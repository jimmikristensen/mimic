package mimic.mountebank.fragment;

import mimic.mountebank.imposter.Response;
import mimic.mountebank.imposter.Stub;

public class ResponseBuilder {

    private Stub stub;
    private Response responses;

    public ResponseBuilder(Stub stub) {
        this.stub = stub;
        responses = new Response();
        this.stub.addResponse(responses);
    }

    public ResponseBuilder status(int status) {
        responses.setStatus(status);
        return this;
    }


}
