package be.ali.boekenwinkel;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("Dit e-mailadres is al geregistreerd.");
    }
}