package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;


public class StandardHttpHeaderVerifier implements HttpHeaderVerifier {

    public Boolean verify(ResponseFields contractResponseFields, ProviderResponse providerResponseFields) {
        return true;
    }
}
