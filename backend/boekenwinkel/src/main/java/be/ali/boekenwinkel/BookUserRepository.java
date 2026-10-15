
package be.ali.boekenwinkel;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookUserRepository extends JpaRepository<BookUser, Long> {

    Optional<BookUser> findByEmail(String email);

}
