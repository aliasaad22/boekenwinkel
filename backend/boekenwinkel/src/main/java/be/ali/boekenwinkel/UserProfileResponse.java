
package be.ali.boekenwinkel;

import java.util.List;

public record UserProfileResponse(
        Long id,
        String name,
        String email,
        List<Reservation> reservations
) {
}
