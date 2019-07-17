package mimic.mountebank.provider.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.provider.verifier.results.HttpBodyVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;

public class HttpJsonBodyVerifier implements MessageBodyVerifier {

    final Logger logger = LoggerFactory.getLogger(HttpJsonBodyVerifier.class);

    @Override
    public HttpBodyVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        boolean isBodyAMatch = isBodyExactMatch(contractResponseFields, providerResponseFields);

        // lenient https://www.baeldung.com/jsonassert

        return null;
    }

    public boolean isBodyExactMatch(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        try {
            String contractBody = contractResponseFields.getBody();
            String providerBody = providerResponseFields.getResponseBody();

            if ((contractBody == null && providerBody != null) || (contractBody != null && providerBody == null)) {
                return false;

            } else if (contractBody == null && providerBody == null) {
                return true;

            } else {
                JsonNode jsonContractBody = JacksonObjectMapper.getMapper().readTree(contractBody);
                JsonNode jsonProviderBody = JacksonObjectMapper.getMapper().readTree(providerBody);
                return jsonContractBody.equals(jsonProviderBody);
            }

        } catch (IOException e) {
            logger.error("Unable to parse json string", e);
            throw new UncheckedIOException(e);
        }
    }
}
