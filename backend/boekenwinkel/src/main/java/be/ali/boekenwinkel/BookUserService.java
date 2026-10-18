
package be.ali.boekenwinkel;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Service
public class BookUserService {

    private final BookUserRepository bookUserRepository;
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;

    private final PasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public BookUserService(
            BookUserRepository bookUserRepository,
            ReservationRepository reservationRepository,
            BookRepository bookRepository) {
        this.bookUserRepository = bookUserRepository;
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
    }

    public BookUser saveUser(BookUser user) {
        if (bookUserRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return bookUserRepository.save(user);
    }

    public Optional<BookUser> getUserByEmail(String email) {
        return bookUserRepository.findByEmail(email);
    }

    public Optional<BookUser> getUserById(Long id) {
        return bookUserRepository.findById(id);
    }

    public boolean checkPassword(
            String rawPassword,
            String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

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
