package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.HttpHeaderVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;
import mimic.mountebank.provider.verifier.results.ReportStatus;

import java.util.Map;


public class HttpHeaderVerifier implements MessageHeaderVerifier {

    private HttpHeaderVerificationResult verificationResult;

    public HttpHeaderVerifier() {
        verificationResult = new HttpHeaderVerificationResult();
    }

    @Override
    public HttpHeaderVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        boolean doesStatusMatch = isStatusExactMatch(contractResponseFields.getStatus(), providerResponseFields.getResponseStatus());
        boolean doesHeadersMatch = isHeadersExactMatch(contractResponseFields.getHeaders(), providerResponseFields.getResponseHeaders());

        if (doesStatusMatch && doesHeadersMatch) {
            verificationResult.setReportStatus(ReportStatus.OK);
        } else {
            verificationResult.setReportStatus(ReportStatus.FAILED);
        }

        return verificationResult;
    }

    private boolean isStatusExactMatch(int contractStatus, int providerStatus) {
        verificationResult.setContractStatusCode(contractStatus);
        verificationResult.setProviderStatusCode(providerStatus);

        if (contractStatus == providerStatus) {
            return true;
        }
        return false;
    }

    private boolean isHeadersExactMatch(Map<String, String> contractHeaders, Map<String, String> providerHeaders) {
        verificationResult.setContractHeaders(contractHeaders);
        verificationResult.setProviderHeaders(providerHeaders);



        return contractHeaders.equals(providerHeaders);
    }

}
