
package be.ali.boekenwinkel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    // Test 1: Onbekend e-mailadres kan niet inloggen
    @Test
    void loginGeeftLegeResultaatBijOnbekendEmail() {
        when(bookUserRepository.findByEmail("onbekend@example.com"))
                .thenReturn(Optional.empty());

        Optional<BookUser> resultaat =
                bookUserService.login(
                        "onbekend@example.com",
                        "wachtwoord"
                );

        assertTrue(resultaat.isEmpty());
    }

    // Test 2: Verkeerd wachtwoord wordt geweigerd
    @Test
    void loginGeeftLegeResultaatBijVerkeerdWachtwoord() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        BookUser gebruiker = new BookUser();
        gebruiker.setEmail("test@example.com");
        gebruiker.setPassword(encoder.encode("juistWachtwoord"));

        when(bookUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(gebruiker));

        Optional<BookUser> resultaat =
                bookUserService.login(
                        "test@example.com",
                        "verkeerdWachtwoord"
                );

        assertTrue(resultaat.isEmpty());
    }

    // Test 3: Correct wachtwoord geeft de gebruiker terug
    @Test
    void loginGeeftGebruikerBijCorrectWachtwoord() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        BookUser gebruiker = new BookUser();
        gebruiker.setEmail("test@example.com");
        gebruiker.setPassword(encoder.encode("juistWachtwoord"));

        when(bookUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(gebruiker));

        Optional<BookUser> resultaat =
                bookUserService.login(
                        "test@example.com",
                        "juistWachtwoord"
                );

        assertTrue(resultaat.isPresent());
        assertEquals("test@example.com", resultaat.get().getEmail());
    }

    // Test 4: Profiel ophalen van een niet-bestaande gebruiker
    @Test
    void getUserProfileGeeftFoutAlsGebruikerNietBestaat() {
        when(bookUserRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> bookUserService.getUserProfile(999L)
        );
    }

    // Test 5: Gekochte boeken ophalen van een niet-bestaande gebruiker
    @Test
    void getPurchasedBooksGeeftFoutAlsGebruikerNietBestaat() {
        when(bookUserRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> bookUserService.getPurchasedBooks(999L)
        );
    }

    // Test 6: Registratie codeert het wachtwoord
    @Test
    void saveUserSlaatGebruikerOpMetGecodeerdWachtwoord() {
        BookUser gebruiker = new BookUser();
        gebruiker.setName("Testgebruiker");
        gebruiker.setEmail("nieuw@example.com");
        gebruiker.setPassword("mijnWachtwoord");

        when(bookUserRepository.findByEmail("nieuw@example.com"))
                .thenReturn(Optional.empty());

        when(bookUserRepository.save(any(BookUser.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookUser resultaat = bookUserService.saveUser(gebruiker);

        assertNotNull(resultaat.getPassword());
        assertNotEquals("mijnWachtwoord", resultaat.getPassword());

        assertTrue(
                bookUserService.checkPassword(
                        "mijnWachtwoord",
                        resultaat.getPassword()
                )
        );

        verify(bookUserRepository).save(gebruiker);
    }

    // Test 7: Een dubbel e-mailadres wordt geweigerd
    @Test
    void saveUserWeigertDubbelEmailadres() {
        BookUser bestaandeGebruiker = new BookUser();
        bestaandeGebruiker.setEmail("bestaand@example.com");

        BookUser nieuweGebruiker = new BookUser();
        nieuweGebruiker.setEmail("bestaand@example.com");
        nieuweGebruiker.setPassword("mijnWachtwoord");

        when(bookUserRepository.findByEmail("bestaand@example.com"))
                .thenReturn(Optional.of(bestaandeGebruiker));

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> bookUserService.saveUser(nieuweGebruiker)
        );

        verify(bookUserRepository, never())
                .save(any(BookUser.class));
    }
}
