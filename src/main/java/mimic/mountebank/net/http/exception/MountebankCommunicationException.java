package mimic.mountebank.net.http.exception;

public class MountebankCommunicationException extends RuntimeException {

    public MountebankCommunicationException(String msg, Throwable err) {
        super(msg, err);
    }

    public MountebankCommunicationException(String msg) {
        super(msg);
    }
}
