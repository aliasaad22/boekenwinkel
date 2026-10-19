
package be.ali.boekenwinkel;

public record ReviewResponse(
        Long id,
        Long userId,
        Long bookId,
        int score,
        String text
) {
}
