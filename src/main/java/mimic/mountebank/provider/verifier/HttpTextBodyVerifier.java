package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;

public class HttpTextBodyVerifier implements MessageBodyVerifier {

    @Override
    public boolean verify(ResponseFields contractResponseFields, ProviderResponse providerResponseFields) {
        String contractBody = contractResponseFields.getBody();
        String providerBody = providerResponseFields.getBody();

        if ((contractBody == null && providerBody != null) || (contractBody != null && providerBody == null)) {
            return false;

        } else if (contractBody == null && providerBody == null) {
            return true;

        } else {
            return contractResponseFields.getBody().equals(providerResponseFields.getBody());
        }
    }
}
