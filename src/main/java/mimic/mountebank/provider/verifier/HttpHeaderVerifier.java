package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;

public interface HttpHeaderVerifier {

    public Boolean verify(ResponseFields contractResponseFields, ProviderResponse providerResponseFields);
}
