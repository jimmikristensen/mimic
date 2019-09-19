package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.HttpTextBodyVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;
import mimic.mountebank.provider.verifier.results.ReportStatus;

public class HttpTextBodyVerifier implements MessageBodyVerifier {

    private HttpTextBodyVerificationResult bodyVerificationResult;

    public HttpTextBodyVerifier() {
        bodyVerificationResult = new HttpTextBodyVerificationResult();
    }

    @Override
    public HttpTextBodyVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        String contractBody = nullToEmptyString(contractResponseFields.getBody());
        String providerBody = nullToEmptyString(providerResponseFields.getResponseBody());
        bodyVerificationResult.setContractBody(contractBody);
        bodyVerificationResult.setProviderBody(providerBody);

        if (contractBody.equals(providerBody)) {
            bodyVerificationResult.setReportStatus(ReportStatus.OK);
        } else {
            bodyVerificationResult.setReportStatus(ReportStatus.FAILED);
        }

        return bodyVerificationResult;
    }

    private String nullToEmptyString(String str) {
        return str == null ? "" : str;
    }
}
