package mimic.mountebank.provider.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.provider.ProviderResponse;

import java.io.IOException;

public class JsonBodyVerifier implements HttpBodyVerifier {

    @Override
    public boolean verify(ResponseFields contractResponseFields, ProviderResponse providerResponseFields) {
        boolean isBodyAMatch = isBodyExactMatch(contractResponseFields, providerResponseFields);

        // lenient https://www.baeldung.com/jsonassert

        return isBodyAMatch;
    }

    public boolean isBodyExactMatch(ResponseFields contractResponseFields, ProviderResponse providerResponseFields) {
        try {
            JsonNode contractBody = JacksonObjectMapper.getMapper().readTree(contractResponseFields.getBody());
            JsonNode providerBody = JacksonObjectMapper.getMapper().readTree(providerResponseFields.getBody());

            return contractBody.equals(providerBody);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
