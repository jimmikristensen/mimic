package mimic.mountebank.provider.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.provider.verifier.results.HttpBodyVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;
import mimic.mountebank.provider.verifier.results.ReportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;

public class HttpJsonBodyVerifier implements MessageBodyVerifier {

    final Logger logger = LoggerFactory.getLogger(HttpJsonBodyVerifier.class);

    private HttpBodyVerificationResult bodyVerificationResult;

    public HttpJsonBodyVerifier() {
        bodyVerificationResult = new HttpBodyVerificationResult();
    }

    @Override
    public HttpBodyVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        return isBodyExactMatch(contractResponseFields, providerResponseFields);

        // lenient https://www.baeldung.com/jsonassert

    }

    public HttpBodyVerificationResult isBodyExactMatch(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
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
                JsonNode jsonContractBody = JacksonObjectMapper.getMapper().readTree(contractBody);
                JsonNode jsonProviderBody = JacksonObjectMapper.getMapper().readTree(providerBody);

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
