package exceptions;

public abstract class AbstractException extends RuntimeException {

    private String businessMessage;

    public AbstractException() {
        super("Abstract exception", null, false, false);
        this.businessMessage = "";
    }

    public AbstractException(String message) {
        super(message, null, false, false);
        this.businessMessage = message;
    }

    protected AbstractException(String message, Exception e) {
        super(message, e, false, false);
        this.businessMessage = message;
    }

    public String getBusinessMessage() {
        return businessMessage;
    }
}
