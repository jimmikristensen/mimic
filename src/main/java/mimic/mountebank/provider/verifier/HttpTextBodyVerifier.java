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
        String contractBody = contractResponseFields.getBody();
        String providerBody = providerResponseFields.getResponseBody();
        bodyVerificationResult.setContractBody(contractBody);
        bodyVerificationResult.setProviderBody(providerBody);

        if ((contractBody == null && providerBody != null) || (contractBody != null && providerBody == null)) {
            bodyVerificationResult.setReportStatus(ReportStatus.FAILED);

        } else if (contractBody == null && providerBody == null) {
            bodyVerificationResult.setReportStatus(ReportStatus.OK);

        } else {
            if (contractResponseFields.getBody().equals(providerResponseFields.getResponseBody())) {
                bodyVerificationResult.setReportStatus(ReportStatus.OK);
            } else {
                bodyVerificationResult.setReportStatus(ReportStatus.FAILED);
            }
        }
        return bodyVerificationResult;
    }
}
