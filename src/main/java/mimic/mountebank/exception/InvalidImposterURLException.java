package mimic.mountebank.exception;

public class InvalidImposterURLException extends RuntimeException {

    public InvalidImposterURLException(String msg, Throwable err) {
        super(msg, err);
    }

    public InvalidImposterURLException(String msg) {
        super(msg);
    }
}
