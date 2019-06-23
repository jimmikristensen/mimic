package mimic.mountebank.net.http.exception;

public class InvalidImposterException extends RuntimeException {

    public InvalidImposterException(String msg, Throwable err) {
        super(msg, err);
    }

    public InvalidImposterException(String msg) {
        super(msg);
    }
}
