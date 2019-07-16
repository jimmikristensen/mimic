package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;

public interface MessageBodyVerifier {

    public boolean verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields);
}
