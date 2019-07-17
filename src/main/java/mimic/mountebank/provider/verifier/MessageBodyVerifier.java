package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.HttpBodyVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;

public interface MessageBodyVerifier {

    public HttpBodyVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields);
}
