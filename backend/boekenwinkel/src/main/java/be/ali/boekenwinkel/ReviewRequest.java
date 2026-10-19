
package be.ali.boekenwinkel;

public record ReviewRequest(
        Long userId,
        Long bookId,
        int score,
        String text
) {
}
