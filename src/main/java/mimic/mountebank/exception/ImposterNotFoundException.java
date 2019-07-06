package mimic.mountebank.exception;

public class ImposterNotFoundException extends RuntimeException {

    public ImposterNotFoundException(String msg, Throwable err) {
        super(msg, err);
    }

    public ImposterNotFoundException(String msg) {
        super(msg);
    }
}
