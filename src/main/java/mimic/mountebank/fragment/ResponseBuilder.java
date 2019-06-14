package mimic.mountebank.fragment;

import mimic.mountebank.imposter.Response;
import mimic.mountebank.imposter.Responses;
import mimic.mountebank.imposter.Stub;

public class ResponseBuilder {

    private Stub stub;
    private Responses responses;
    private Response response;

    public ResponseBuilder(Stub stub) {
        this.stub = stub;
        responses = new Responses();
        response = new Response();
        responses.addResponse(response);
        this.stub.addResponse(responses);
    }

    public ResponseBuilder status(int status) {
        response.setStatus(status);
        return this;
    }


}
