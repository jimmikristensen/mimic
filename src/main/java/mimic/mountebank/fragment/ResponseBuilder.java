package mimic.mountebank.fragment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        responses.setResponse(response);
        this.stub.addResponse(responses);
    }

    public ResponseBuilder status(int status) {
        response.setStatus(status);
        return this;
    }

    public ResponseBuilder body(String body) {
        response.setBody(body);
        return this;
    }

    public ResponseBuilder body(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writer().writeValueAsString(jsonNode);
        response.setBody(jsonString);
        return this;
    }

    public ResponseBuilder header(String key, String value) {
        response.addHeader(key, value);
        return this;
    }
}
