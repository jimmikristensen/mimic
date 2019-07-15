package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;

public interface MessageBodyVerifier {

    public boolean verify(ResponseFields contractResponseFields, ProviderResponse providerResponseFields);
}
