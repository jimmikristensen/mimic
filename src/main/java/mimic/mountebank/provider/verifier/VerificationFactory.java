package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;
import mimic.mountebank.provider.verifier.net.http.HTTPClient;


public interface VerificationFactory {

    public MessageHeaderVerifier createHeaderVerifier();

    public MessageBodyVerifier createBodyVerifier(ResponseFields contractResponse, ProviderResponse providerResponse);

    public HTTPClient createHttpClient();
}
