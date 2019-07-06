package mimic.mountebank.exception;

public class ImposterParseException extends RuntimeException {

    public ImposterParseException(String msg, Throwable err) {
        super(msg, err);
    }

    public ImposterParseException(String msg) {
        super(msg);
    }
}
