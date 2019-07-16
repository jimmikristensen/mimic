package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;

public class HttpTextBodyVerifier implements MessageBodyVerifier {

    @Override
    public boolean verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields) {
        String contractBody = contractResponseFields.getBody();
        String providerBody = providerResponseFields.getResponseBody();

        if ((contractBody == null && providerBody != null) || (contractBody != null && providerBody == null)) {
            return false;

        } else if (contractBody == null && providerBody == null) {
            return true;

        } else {
            return contractResponseFields.getBody().equals(providerResponseFields.getResponseBody());
        }
    }
}
