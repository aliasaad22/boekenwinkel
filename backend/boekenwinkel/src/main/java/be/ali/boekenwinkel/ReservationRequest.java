package be.ali.boekenwinkel;

public record ReservationRequest(
        Long userId,
        Long bookId,
        String pickupDate
) {
}