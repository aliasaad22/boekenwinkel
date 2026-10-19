
package be.ali.boekenwinkel;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByUserIdAndPaymentStatus(
            Long userId,
            String paymentStatus
    );

    boolean existsByUserIdAndBookIdAndPaymentStatus(
            Long userId,
            Long bookId,
            String paymentStatus
    );

}
