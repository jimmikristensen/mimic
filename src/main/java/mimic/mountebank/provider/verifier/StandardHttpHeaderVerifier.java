package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;

import java.util.Map;


public class StandardHttpHeaderVerifier implements HttpHeaderVerifier {

    @Override
    public Boolean verify(ResponseFields contractResponseFields, ProviderResponse providerResponseFields) {
        boolean doesStatusMatch = isStatusExactMatch(contractResponseFields.getStatus(), providerResponseFields.getStatus());
        boolean doesHeadersMatch = isHeadersExactMatch(contractResponseFields.getHeaders(), providerResponseFields.getHeaders());

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
