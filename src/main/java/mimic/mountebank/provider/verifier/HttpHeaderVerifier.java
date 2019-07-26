package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.HttpHeaderVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;
import mimic.mountebank.provider.verifier.results.ReportStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class HttpHeaderVerifier implements MessageHeaderVerifier {

    private HttpHeaderVerificationResult verificationResult;

    public HttpHeaderVerifier() {
        verificationResult = new HttpHeaderVerificationResult();
    }

    @Override
    public HttpHeaderVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        boolean doesStatusMatch = isStatusExactMatch(contractResponseFields.getStatus(), providerResponseFields.getResponseStatus());
        boolean doesHeadersMatch = isHeadersMatch(contractResponseFields.getHeaders(), providerResponseFields.getResponseHeaders());

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

    private boolean isHeadersMatch(Map<String, String> contractHeaders, Map<String, String> providerHeaders) {
        verificationResult.setContractHeaders(contractHeaders);
        verificationResult.setProviderHeaders(providerHeaders);

        // if contract doesn't have any headers
        if (contractHeaders == null) return true;

        // find any missing headers on the provider side
        providerHeaders = providerHeaders != null ? providerHeaders : new LinkedHashMap<>();
        Map<String, String> missingHeadersFromProvider = new HashMap<>(contractHeaders);
        missingHeadersFromProvider.keySet().removeAll(providerHeaders.keySet());
        if (missingHeadersFromProvider.size() > 0) {
            return false;
        }

        // find out if the contract header values matches that of the provider headers
        Map<String, String> ph = providerHeaders;
        boolean headerValueMatch = contractHeaders.entrySet().stream()
                .allMatch(e -> ph.get(e.getKey()) != null && e.getValue().equals(ph.get(e.getKey())));

        return headerValueMatch;
    }

}
