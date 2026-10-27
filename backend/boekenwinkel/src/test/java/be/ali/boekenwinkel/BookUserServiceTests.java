
package be.ali.boekenwinkel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BookUserServiceTests {

    @Mock
    private BookUserRepository bookUserRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookUserService bookUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginGeeftLegeResultaatBijOnbekendEmail() {
        when(bookUserRepository.findByEmail("onbekend@example.com"))
                .thenReturn(Optional.empty());

        Optional<BookUser> resultaat =
                bookUserService.login("onbekend@example.com", "wachtwoord");

        assertTrue(resultaat.isEmpty());
    }

    @Test
    void loginGeeftLegeResultaatBijVerkeerdWachtwoord() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        BookUser gebruiker = new BookUser();
        gebruiker.setEmail("test@example.com");
        gebruiker.setPassword(encoder.encode("juistWachtwoord"));

        when(bookUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(gebruiker));

        Optional<BookUser> resultaat =
                bookUserService.login("test@example.com", "verkeerdWachtwoord");

        assertTrue(resultaat.isEmpty());
    }

    @Test
    void loginGeeftGebruikerBijCorrectWachtwoord() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        BookUser gebruiker = new BookUser();
        gebruiker.setEmail("test@example.com");
        gebruiker.setPassword(encoder.encode("juistWachtwoord"));

        when(bookUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(gebruiker));

        Optional<BookUser> resultaat =
                bookUserService.login("test@example.com", "juistWachtwoord");

        assertTrue(resultaat.isPresent());
        assertEquals("test@example.com", resultaat.get().getEmail());
    }

    @Test
    void getUserProfileGeeftFoutAlsGebruikerNietBestaat() {
        when(bookUserRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> bookUserService.getUserProfile(999L)
        );
    }

    @Test
    void getPurchasedBooksGeeftFoutAlsGebruikerNietBestaat() {
        when(bookUserRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> bookUserService.getPurchasedBooks(999L)
        );
    }
}
