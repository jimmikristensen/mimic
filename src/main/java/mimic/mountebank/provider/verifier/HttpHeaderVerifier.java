package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;

import java.util.Map;


public class HttpHeaderVerifier implements MessageHeaderVerifier {

    @Override
    public Boolean verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        boolean doesStatusMatch = isStatusExactMatch(contractResponseFields.getStatus(), providerResponseFields.getResponseStatus());
        boolean doesHeadersMatch = isHeadersExactMatch(contractResponseFields.getHeaders(), providerResponseFields.getResponseHeaders());

        return doesStatusMatch && doesHeadersMatch;
    }

    private boolean isStatusExactMatch(int contractStatus, int providerStatus) {
        if (contractStatus == providerStatus) {
            return true;
        }
        return false;
    }

    private boolean isHeadersExactMatch(Map<String, String> contractHeaders, Map<String, String> providerHeaders) {
        return contractHeaders.equals(providerHeaders);
    }

}
