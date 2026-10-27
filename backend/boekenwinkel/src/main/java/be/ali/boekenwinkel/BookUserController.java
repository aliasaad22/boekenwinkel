package be.ali.boekenwinkel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@RestController
@RequestMapping("/users")
public class BookUserController {

    private final BookUserService bookUserService;

    public BookUserController(BookUserService bookUserService) {
        this.bookUserService = bookUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookUserResponse registerUser(@RequestBody BookUser user) {
        BookUser savedUser = bookUserService.saveUser(user);

        return new BookUserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }
    @GetMapping
    public BookUserResponse getUserByEmail(
            @RequestParam String email) {

        BookUser user = bookUserService.getUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Gebruiker niet gevonden."
                ));

        return new BookUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
    @GetMapping("/{id}")
    public BookUserResponse getUserById(@PathVariable Long id) {
        BookUser user = bookUserService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Gebruiker niet gevonden."
                ));

        return new BookUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
    @PostMapping("/login")
    public BookUserResponse login(
            @RequestBody BookUser user,
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response) {

        BookUser loggedInUser = bookUserService.login(
                user.getEmail(),
                user.getPassword()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "E-mailadres of wachtwoord is onjuist."
        ));

        var authentication =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        loggedInUser.getEmail(),
                        null,
                        java.util.List.of(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER")
                        )
                );

        var securityContext =
                org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        org.springframework.security.core.context.SecurityContextHolder.setContext(
                securityContext
        );

        request.getSession(true).setAttribute(
                "SPRING_SECURITY_CONTEXT",
                securityContext
        );

        return new BookUserResponse(
                loggedInUser.getId(),
                loggedInUser.getName(),
                loggedInUser.getEmail()
        );
    }

    @GetMapping("/{id}/profile")
    public UserProfileResponse getUserProfile(@PathVariable Long id) {
        return bookUserService.getUserProfile(id);
    }

    @GetMapping("/{id}/purchased-books")
    public List<PurchasedBookResponse> getPurchasedBooks(
            @PathVariable Long id) {
        return bookUserService.getPurchasedBooks(id);
    }


}