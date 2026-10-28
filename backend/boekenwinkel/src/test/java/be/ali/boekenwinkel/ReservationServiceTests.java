
package be.ali.boekenwinkel;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTests {

    @Test
    void bevestigenVanReserveringZonderVoorraadGeeftConflict() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Reservation reservation = new Reservation();
        reservation.setBookId(1L);
        reservation.setStatus("PENDING");

        Book book = new Book();
        book.setStock(0);

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));
        when(bookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.confirmReservation(1L)
        );

        assertEquals(409, exception.getStatusCode().value());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void betaaldeReserveringKanNietOpnieuwBetaaldWorden() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Reservation reservation = new Reservation();
        reservation.setStatus("CONFIRMED");
        reservation.setPaymentStatus("PAID");

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.markAsPaid(1L)
        );

        assertEquals(409, exception.getStatusCode().value());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void bevestigenVanReserveringMetVoorraadWerkt() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Reservation reservation = new Reservation();
        reservation.setBookId(1L);
        reservation.setStatus("PENDING");

        Book book = new Book();
        book.setStock(5);

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));
        when(bookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(bookRepository.save(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = service.confirmReservation(1L);

        assertEquals("CONFIRMED", result.getStatus());
        assertEquals(4, book.getStock());
        verify(bookRepository).save(book);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void bevestigdeReserveringKanNietOpnieuwBevestigdWorden() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Reservation reservation = new Reservation();
        reservation.setBookId(1L);
        reservation.setStatus("CONFIRMED");

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.confirmReservation(1L)
        );

        assertEquals(409, exception.getStatusCode().value());
        verify(bookRepository, never()).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void reserveringDieNietBestaatGeeftFout() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(reservationRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> service.confirmReservation(99L)
        );

        verify(bookRepository, never()).findById(anyLong());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void reserveringKanNietBetaaldWordenVoorBevestiging() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Reservation reservation = new Reservation();
        reservation.setStatus("PENDING");
        reservation.setPaymentStatus("NOT_PAID");

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.markAsPaid(1L)
        );

        assertEquals(409, exception.getStatusCode().value());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void bevestigdeReserveringKanSuccesvolBetaaldWorden() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Reservation reservation = new Reservation();
        reservation.setStatus("CONFIRMED");
        reservation.setPaymentStatus("NOT_PAID");

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = service.markAsPaid(1L);

        assertEquals("PAID", result.getPaymentStatus());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void betalingVanReserveringDieNietBestaatGeeftFout() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        when(reservationRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> service.markAsPaid(99L)
        );

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void reserveringMetOnbekendBoekKanNietBevestigdWorden() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Reservation reservation = new Reservation();
        reservation.setBookId(99L);
        reservation.setStatus("PENDING");

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));
        when(bookRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> service.confirmReservation(1L)
        );

        verify(bookRepository, never()).save(any(Book.class));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
    @Test
    void nieuweReserveringWordtAutomatischBevestigdEnVermindertVoorraad() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Book book = new Book();
        book.setStock(5);

        when(bookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));
        when(bookRepository.save(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(bookUserRepository.findById(2L))
                .thenReturn(java.util.Optional.of(new BookUser()));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = service.createReservation(
                2L,
                1L,
                java.time.LocalDate.now().toString()
        );

        assertEquals("CONFIRMED", result.getStatus());
        assertEquals("NOT_PAID", result.getPaymentStatus());
        assertEquals(4, book.getStock());

        verify(bookRepository).save(book);
        verify(reservationRepository).save(result);
    }
    @Test
    void betalingVanReserveringMetOnbekendeStatusWordtGeweigerd() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        BookUserRepository bookUserRepository = mock(BookUserRepository.class);

        ReservationService service = new ReservationService(
                reservationRepository,
                bookRepository,
                bookUserRepository
        );

        Reservation reservation = new Reservation();
        reservation.setStatus("PENDING");
        reservation.setPaymentStatus("NOT_PAID");

        when(reservationRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reservation));

        assertThrows(
                ResponseStatusException.class,
                () -> service.markAsPaid(1L)
        );

        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}
