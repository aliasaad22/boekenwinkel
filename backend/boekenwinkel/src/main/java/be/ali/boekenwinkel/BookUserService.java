
package be.ali.boekenwinkel;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * Bevat de servicelogica voor het registreren en opzoeken van gebruikers,
 * het controleren van wachtwoorden en het ophalen van gebruikersgegevens.
 */
@Service
public class BookUserService {

    private final BookUserRepository bookUserRepository;
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;

    private final PasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    /**
     * Maakt de service aan met de benodigde repositories.
     *
     * @param bookUserRepository repository voor gebruikers
     * @param reservationRepository repository voor reserveringen
     * @param bookRepository repository voor boeken
     */
    public BookUserService(
            BookUserRepository bookUserRepository,
            ReservationRepository reservationRepository,
            BookRepository bookRepository) {
        this.bookUserRepository = bookUserRepository;
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Registreert een nieuwe gebruiker.
     * Controleert of het e-mailadres al bestaat en slaat het wachtwoord
     * gehasht op.
     *
     * @param user de gebruiker die geregistreerd moet worden
     * @return de opgeslagen gebruiker
     * @throws ResponseStatusException met HTTP-status 409 als het e-mailadres al bestaat
     */
    public BookUser saveUser(BookUser user) {
        if (bookUserRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Dit e-mailadres is al in gebruik."
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return bookUserRepository.save(user);
    }

    /**
     * Zoekt een gebruiker op basis van het e-mailadres.
     *
     * @param email het e-mailadres van de gebruiker
     * @return een Optional met de gebruiker, of leeg als die niet bestaat
     */
    public Optional<BookUser> getUserByEmail(String email) {
        return bookUserRepository.findByEmail(email);
    }

    /**
     * Zoekt een gebruiker op basis van het ID.
     *
     * @param id het ID van de gebruiker
     * @return een Optional met de gebruiker, of leeg als die niet bestaat
     */
    public Optional<BookUser> getUserById(Long id) {
        return bookUserRepository.findById(id);
    }

    /**
     * Vergelijkt een ingevoerd wachtwoord met het gehashte wachtwoord.
     *
     * @param rawPassword het ingevoerde wachtwoord
     * @param encodedPassword het opgeslagen gehashte wachtwoord
     * @return true als de wachtwoorden overeenkomen, anders false
     */
    public boolean checkPassword(
            String rawPassword,
            String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Probeert een gebruiker aan te melden met e-mailadres en wachtwoord.
     *
     * @param email het e-mailadres van de gebruiker
     * @param rawPassword het ingevoerde wachtwoord
     * @return een Optional met de gebruiker bij geldige gegevens,
     *         of leeg bij ongeldige gegevens
     */
    public Optional<BookUser> login(
            String email,
            String rawPassword) {
        Optional<BookUser> userOptional =
                bookUserRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        BookUser user = userOptional.get();

        if (!checkPassword(rawPassword, user.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    /**
     * Haalt het profiel en de reserveringen van een gebruiker op.
     *
     * @param userId het ID van de gebruiker
     * @return het profiel met gebruikersgegevens en reserveringen
     * @throws ResponseStatusException als de gebruiker niet bestaat (404)
     */
    public UserProfileResponse getUserProfile(Long userId) {
        BookUser user = getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Gebruiker niet gevonden."
                ));

        List<Reservation> reservations =
                reservationRepository.findByUserId(userId);

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                reservations
        );
    }

    /**
     * Haalt de betaalde boeken van een gebruiker op.
     *
     * @param userId het ID van de gebruiker
     * @return een lijst met aangekochte boeken
     * @throws ResponseStatusException als de gebruiker niet bestaat (404)
     */
    public List<PurchasedBookResponse> getPurchasedBooks(Long userId) {
        getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Gebruiker niet gevonden."
                ));

        List<Reservation> paidReservations =
                reservationRepository.findByUserIdAndPaymentStatus(
                        userId,
                        "PAID"
                );

        List<PurchasedBookResponse> purchasedBooks = new ArrayList<>();

        for (Reservation reservation : paidReservations) {
            Book book = bookRepository.findById(reservation.getBookId())
                    .orElse(null);

            if (book != null) {
                purchasedBooks.add(new PurchasedBookResponse(
                        reservation.getId(),
                        book.getId(),
                        book.getTitle(),
                        book.getAuthors(),
                        book.getPrice()
                ));
            }
        }

        return purchasedBooks;
    }
}
