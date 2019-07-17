package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.results.HttpHeaderVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;

public interface MessageHeaderVerifier {

    public HttpHeaderVerificationResult verify(ResponseFields contractResponseFields, ProviderHTTPResult providerResponseFields);
}
