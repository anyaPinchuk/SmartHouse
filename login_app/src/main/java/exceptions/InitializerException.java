package exceptions;

public class InitializerException extends Exception {
    public InitializerException(Exception e) {
        super("The exception occurred in Initializer: Initialization failed", e, false, false);
    }
}
