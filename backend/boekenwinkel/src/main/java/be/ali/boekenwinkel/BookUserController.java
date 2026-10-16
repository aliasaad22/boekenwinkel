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
}