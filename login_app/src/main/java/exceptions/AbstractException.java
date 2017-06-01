package exceptions;

public abstract class AbstractException extends RuntimeException {

    private String businessMessage;

    AbstractException() {
        super("Abstract exception", null, false, false);
        this.businessMessage = "";
    }

    AbstractException(String message) {
        super(message, null, false, false);
        this.businessMessage = message;
    }

    AbstractException(String message, Exception e) {
        super(message, e, false, false);
        this.businessMessage = message;
    }

    public String getBusinessMessage() {
        return businessMessage;
    }
}
