package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.BodyVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;

public interface MessageBodyVerifier {

    public BodyVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields);
}
