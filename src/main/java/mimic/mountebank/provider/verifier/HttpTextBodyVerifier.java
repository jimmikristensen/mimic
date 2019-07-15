package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;

public class HttpTextBodyVerifier implements MessageBodyVerifier {

    @Override
    public boolean verify(ResponseFields contractResponseFields, ProviderResponse providerResponseFields) {
        return contractResponseFields.getBody().equals(providerResponseFields.getBody());
    }
}
