package be.ali.boekenwinkel;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class BookUserService {

    private final BookUserRepository bookUserRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public BookUserService(BookUserRepository bookUserRepository) {
        this.bookUserRepository = bookUserRepository;
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
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
