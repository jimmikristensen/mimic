package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;

import java.net.http.HttpClient;

public interface VerificationFactory {

    public MessageHeaderVerifier createHttpHeaderVerifier();

    public MessageBodyVerifier createHttpBodyVerifier(ResponseFields contractResponse, ProviderResponse providerResponse);

    public HttpClient createHttpClient();
}
