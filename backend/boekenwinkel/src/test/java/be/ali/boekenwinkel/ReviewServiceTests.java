
package be.ali.boekenwinkel;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTests {

    @Test
    void reviewsOphalenGeeftReviewsVanHetBoekTerug() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Review review1 = new Review();
        Review review2 = new Review();

        when(reviewRepository.findByBookId(1L))
                .thenReturn(List.of(review1, review2));

        List<Review> result = service.getReviewsByBookId(1L);

        assertEquals(2, result.size());
        verify(reviewRepository).findByBookId(1L);
    }

    @Test
    void gemiddeldeScoreWordtCorrectBerekend() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Review review1 = new Review();
        review1.setScore(4);

        Review review2 = new Review();
        review2.setScore(2);

        when(reviewRepository.findByBookId(1L))
                .thenReturn(List.of(review1, review2));

        double result = service.getAverageScoreByBookId(1L);

        assertEquals(3.0, result);
    }

    @Test
    void gemiddeldeScoreZonderReviewsIsNul() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(reviewRepository.findByBookId(1L))
                .thenReturn(List.of());

        assertEquals(0.0, service.getAverageScoreByBookId(1L));
    }

    @Test
    void geldigeReviewWordtOpgeslagen() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Review review = new Review();
        review.setUserId(1L);
        review.setBookId(2L);
        review.setScore(5);

        when(bookUserRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsById(2L)).thenReturn(true);
        when(reservationRepository.existsByUserIdAndBookIdAndPaymentStatus(
                1L, 2L, "PAID"
        )).thenReturn(true);
        when(reviewRepository.existsByUserIdAndBookId(1L, 2L))
                .thenReturn(false);
        when(reviewRepository.save(review)).thenReturn(review);

        Review result = service.saveReview(review);

        assertSame(review, result);
        verify(reviewRepository).save(review);
    }

    @Test
    void reviewMetScoreNulWordtGeweigerd() {
        ReviewService service = maakReviewService();

        Review review = maakReview(1L, 2L, 0);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveReview(review)
        );

        assertEquals(400, exception.getStatusCode().value());
    }

    @Test
    void reviewMetScoreZesWordtGeweigerd() {
        ReviewService service = maakReviewService();

        Review review = maakReview(1L, 2L, 6);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveReview(review)
        );

        assertEquals(400, exception.getStatusCode().value());
    }

    @Test
    void reviewVanOnbekendeGebruikerWordtGeweigerd() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(bookUserRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveReview(maakReview(1L, 2L, 5))
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(bookRepository, never()).existsById(anyLong());
    }

    @Test
    void reviewVanOnbekendBoekWordtGeweigerd() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(bookUserRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsById(2L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveReview(maakReview(1L, 2L, 5))
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void reviewZonderBetaaldeReserveringWordtGeweigerd() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(bookUserRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsById(2L)).thenReturn(true);
        when(reservationRepository.existsByUserIdAndBookIdAndPaymentStatus(
                1L, 2L, "PAID"
        )).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveReview(maakReview(1L, 2L, 5))
        );

        assertEquals(403, exception.getStatusCode().value());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void dubbeleReviewWordtGeweigerd() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(bookUserRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsById(2L)).thenReturn(true);
        when(reservationRepository.existsByUserIdAndBookIdAndPaymentStatus(
                1L, 2L, "PAID"
        )).thenReturn(true);
        when(reviewRepository.existsByUserIdAndBookId(1L, 2L))
                .thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveReview(maakReview(1L, 2L, 5))
        );

        assertEquals(409, exception.getStatusCode().value());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void bestaandeReviewVerwijderenWerkt() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(reviewRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> service.verwijderReview(1L));

        verify(reviewRepository).deleteById(1L);
    }

    @Test
    void onbekendeReviewVerwijderenGeeftFout() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReviewService service = new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(reviewRepository.existsById(99L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.verwijderReview(99L)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(reviewRepository, never()).deleteById(anyLong());
    }

    private ReviewService maakReviewService() {
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        return new ReviewService(
                reviewRepository,
                reservationRepository,
                bookRepository,
                bookUserRepository
        );
    }

    private Review maakReview(Long userId, Long bookId, int score) {
        Review review = new Review();
        review.setUserId(userId);
        review.setBookId(bookId);
        review.setScore(score);
        return review;
    }
}
