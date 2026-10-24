
package be.ali.boekenwinkel;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTests {

    @Test
    void bevestigenVanReserveringZonderVoorraadGeeftConflict() {
        // We maken nep-repositories voor de test.
        ReservationRepository reservationRepository =
                mock(ReservationRepository.class);
        BookRepository bookRepository =
                mock(BookRepository.class);

        ReservationService reservationService =
                new ReservationService(
                        reservationRepository,
                        bookRepository
                );

        // We maken een reservering die nog niet verwerkt is.
        Reservation reservation = new Reservation();
        reservation.setBookId(1L);
        reservation.setStatus("PENDING");

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));

        // Het boek heeft geen voorraad meer.
        Book book = new Book();
        book.setStock(0);

        when(bookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        // Bevestigen moet een conflict opleveren.
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> reservationService.confirmReservation(1L)
        );

        assertEquals(409, exception.getStatusCode().value());

        // De voorraad mag niet opgeslagen worden.
        verify(bookRepository, never()).save(any(Book.class));
    }
}
