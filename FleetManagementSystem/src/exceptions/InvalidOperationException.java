package exceptions;

public class InvalidOperationException extends Exception {
    public InvalidOperationException(String displayMessage) {
        super(displayMessage);
    }
}
