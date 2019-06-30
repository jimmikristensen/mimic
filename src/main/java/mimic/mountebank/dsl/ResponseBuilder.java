package mimic.mountebank.dsl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mimic.mountebank.ConsumerImposterBuilder;
import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.imposter.Response;
import mimic.mountebank.imposter.Stub;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;

public class ResponseBuilder {

    private Stub stub;
    private Response responses;
    private ResponseFields response;

    public ResponseBuilder(Stub stub) {
        this.stub = stub;
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

    public ResponseBuilder respondsWith() {
        responses = new Response();
        response = new ResponseFields();
        responses.setFields(response);
        stub.addResponse(responses);
        return this;
    }

    public String toImposterString() throws IOException {
        return ConsumerImposterBuilder.getImposterAsJsonString();
    }

    public GenericContainer toMountebank() throws IOException {
        return ConsumerImposterBuilder.postImposterToMountebank();
    }

    public String toMountebank(String mbManagementUrl) throws IOException {
        return ConsumerImposterBuilder.postImposterToMountebank(mbManagementUrl);
    }
}
