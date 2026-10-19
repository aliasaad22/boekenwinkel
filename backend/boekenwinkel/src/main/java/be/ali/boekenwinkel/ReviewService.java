
package be.ali.boekenwinkel;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
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
