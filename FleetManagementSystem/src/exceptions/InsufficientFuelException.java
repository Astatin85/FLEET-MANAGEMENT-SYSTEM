package exceptions;

public class InsufficientFuelException extends Exception {
    public InsufficientFuelException(String displayMessage){
        super(displayMessage);
    }
}
