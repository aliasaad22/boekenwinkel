
package be.ali.boekenwinkel;

import java.math.BigDecimal;

public record PurchasedBookResponse(
        Long reservationId,
        Long bookId,
        String title,
        String authors,
        BigDecimal price
) {
}
