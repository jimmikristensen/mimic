package mimic.mountebank.fragment;

import mimic.mountebank.ConsumerImposterBuilder;

import java.util.List;

public class Response {

    private int status;
    private ConsumerImposterBuilder cib;

    public Response(ConsumerImposterBuilder cib) {
        this.cib = cib;
    }

    public Response status(int status) {
        this.status = status;
        return this;
    }


}
