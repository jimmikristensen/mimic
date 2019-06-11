package mimic.mountebank.fragment;

public class Response {

    private int status;

    public Response status(int status) {
        this.status = status;
        return this;
    }

    public String toImposterString() {
        return null;
    }
}
