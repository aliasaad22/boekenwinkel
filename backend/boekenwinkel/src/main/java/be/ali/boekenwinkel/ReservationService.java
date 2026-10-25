
package be.ali.boekenwinkel;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Bevat de servicelogica voor het beheren van boekreserveringen.
 * Gebruikt de repositories om reserveringen en de voorraad van boeken
 * op te halen en op te slaan.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;

    /**
     * Maakt de service aan met de benodigde repositories.
     *
     * @param reservationRepository repository voor reserveringen
     * @param bookRepository repository voor boeken
     */
    public ReservationService(
            ReservationRepository reservationRepository,
            BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Maakt een reservering aan als het boek bestaat en op voorraad is.
     *
     * @param userId ID van de gebruiker
     * @param bookId ID van het boek
     * @param pickupDate afgesproken ophaaldatum
     * @return de opgeslagen reservering
     * @throws ResponseStatusException als het boek niet bestaat (404)
     *         of niet op voorraad is (409)
     */
    public Reservation createReservation(
            Long userId,
            Long bookId,
            String pickupDate
    ) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Boek niet gevonden."
                ));

        if (book.getStock() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Dit boek is niet op voorraad."
            );
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setBookId(bookId);
        reservation.setPickupDate(pickupDate);

        return reservationRepository.save(reservation);
    }

    /**
     * Bevestigt een reservering en vermindert de voorraad met één.
     *
     * @param reservationId ID van de reservering
     * @return de opgeslagen, bevestigde reservering
     * @throws ResponseStatusException als de reservering of het boek niet
     *         bestaat (404), de reservering al verwerkt is (409),
     *         of het boek niet op voorraad is (409)
     */
    public Reservation confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reservering niet gevonden."
                ));

        if (!"PENDING".equals(reservation.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Deze reservering is al verwerkt."
            );
        }

        Book book = bookRepository.findById(reservation.getBookId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Boek niet gevonden."
                ));

        if (book.getStock() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Dit boek is niet op voorraad."
            );
        }

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        reservation.setStatus("CONFIRMED");
        return reservationRepository.save(reservation);
    }

    /**
     * Haalt alle reserveringen op.
     *
     * @return een lijst met alle reserveringen
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Registreert de betaling van een bevestigde reservering.
     *
     * @param reservationId ID van de reservering
     * @return de opgeslagen, betaalde reservering
     * @throws ResponseStatusException als de reservering niet bestaat (404),
     *         niet bevestigd is (409) of al betaald is (409)
     */
    public Reservation markAsPaid(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reservering niet gevonden."
                ));

        if (!"CONFIRMED".equals(reservation.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Alleen bevestigde reserveringen kunnen betaald worden."
            );
        }

        if ("PAID".equals(reservation.getPaymentStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Deze reservering is al betaald."
            );
        }

        reservation.setPaymentStatus("PAID");

        return reservationRepository.save(reservation);
    }

    /**
     * Haalt alle reserveringen van een bepaalde gebruiker op.
     *
     * @param userId ID van de gebruiker
     * @return een lijst met reserveringen van die gebruiker
     */
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
}
