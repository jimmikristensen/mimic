package mimic.mountebank.provider.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.provider.verifier.results.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;

public class HttpJsonBodyVerifier implements MessageBodyVerifier {

    final Logger logger = LoggerFactory.getLogger(HttpJsonBodyVerifier.class);

    private BodyVerificationResult bodyVerificationResult;

    public HttpJsonBodyVerifier() {
        bodyVerificationResult = new HttpJsonBodyVerificationResult();
    }

    @Override
    public BodyVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        return isBodyExactMatch(contractResponseFields, providerResponseFields);

        // lenient https://www.baeldung.com/jsonassert

    }

    public BodyVerificationResult isBodyExactMatch(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        try {
            String contractBody = contractResponseFields.getBody();
            String providerBody = providerResponseFields.getResponseBody();
            bodyVerificationResult.setContractBody(contractBody);
            bodyVerificationResult.setProviderBody(providerBody);

            if ((contractBody == null && providerBody != null) || (contractBody != null && providerBody == null)) {
                bodyVerificationResult.setReportStatus(ReportStatus.FAILED);

            } else if (contractBody == null && providerBody == null) {
                bodyVerificationResult.setReportStatus(ReportStatus.OK);

            } else {
                ObjectMapper mapper = JacksonObjectMapper.getMapper();

                JsonNode jsonContractBody = mapper.readTree(contractBody);
                JsonNode jsonProviderBody = mapper.readTree(providerBody);

                if (jsonProviderBody == null) {
                    jsonProviderBody = mapper.createObjectNode();
                }

                if (jsonContractBody == null) {
                    jsonContractBody = mapper.createObjectNode();
                }

                if (jsonContractBody.equals(jsonProviderBody)) {
                    bodyVerificationResult.setReportStatus(ReportStatus.OK);
                } else {
                    bodyVerificationResult.setReportStatus(ReportStatus.FAILED);
                }
            }

            return bodyVerificationResult;

        } catch (IOException e) {
            logger.error("Unable to parse json string", e);
            throw new UncheckedIOException(e);
        }
    }
}
