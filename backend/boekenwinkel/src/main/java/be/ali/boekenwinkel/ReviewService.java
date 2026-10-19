
package be.ali.boekenwinkel;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final BookUserRepository bookUserRepository;

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

    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

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


        return reviewRepository.save(review);
    }
}
