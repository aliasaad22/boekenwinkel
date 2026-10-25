
package be.ali.boekenwinkel;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Bevat de servicelogica voor het ophalen, opslaan en verwijderen van reviews.
 * Controleert onder andere de score, het bestaan van de gebruiker en het boek,
 * en of de gebruiker het boek heeft gekocht.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final BookUserRepository bookUserRepository;

    /**
     * Maakt de service aan met de benodigde repositories.
     *
     * @param reviewRepository repository voor reviews
     * @param reservationRepository repository voor reserveringen en betalingen
     * @param bookRepository repository voor boeken
     * @param bookUserRepository repository voor gebruikers
     */
    public ReviewService(
            ReviewRepository reviewRepository,
            ReservationRepository reservationRepository,
            BookRepository bookRepository,
            BookUserRepository bookUserRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.bookUserRepository = bookUserRepository;
    }

    /**
     * Haalt alle reviews van een bepaald boek op.
     *
     * @param bookId ID van het boek
     * @return een lijst met reviews voor het boek
     */
    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    /**
     * Controleert en slaat een review op.
     * Een review moet een score van 1 tot en met 5 hebben.
     * De gebruiker moet bestaan, het boek moet bestaan en de gebruiker
     * moet het boek betaald hebben. Per gebruiker is één review per boek toegestaan.
     *
     * @param review de review die opgeslagen moet worden
     * @return de opgeslagen review
     * @throws ResponseStatusException bij een ongeldige score (400),
     *         een onbekende gebruiker of boek (404), geen betaalde aankoop (403)
     *         of een dubbele review (409)
     */
    public Review saveReview(Review review) {

        if (review.getScore() < 1 || review.getScore() > 5) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "De score moet tussen 1 en 5 liggen."
            );
        }

        if (!bookUserRepository.existsById(review.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Gebruiker niet gevonden."
            );
        }

        if (!bookRepository.existsById(review.getBookId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Boek niet gevonden."
            );
        }

        boolean hasPurchasedBook =
                reservationRepository.existsByUserIdAndBookIdAndPaymentStatus(
                        review.getUserId(),
                        review.getBookId(),
                        "PAID"
                );

        if (!hasPurchasedBook) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Je moet het boek eerst gekocht hebben om een review te plaatsen."
            );
        }

        boolean alreadyReviewed =
                reviewRepository.existsByUserIdAndBookId(
                        review.getUserId(),
                        review.getBookId()
                );

        if (alreadyReviewed) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Je hebt al een review voor dit boek geplaatst."
            );
        }

        return reviewRepository.save(review);
    }

    /**
     * Berekent de gemiddelde score van de reviews voor een boek.
     *
     * @param bookId ID van het boek
     * @return de gemiddelde score, of 0.0 als er geen reviews zijn
     */
    public double getAverageScoreByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);

        return reviews.stream()
                .mapToInt(Review::getScore)
                .average()
                .orElse(0.0);
    }

    /**
     * Verwijdert een review op basis van het ID.
     *
     * @param id ID van de te verwijderen review
     * @throws ResponseStatusException als de review niet bestaat (404)
     */
    public void verwijderReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Review niet gevonden."
            );
        }

        reviewRepository.deleteById(id);
    }
}
