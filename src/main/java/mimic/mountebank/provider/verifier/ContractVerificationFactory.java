package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;

public interface ContractVerificationFactory {

    public HttpHeaderVerifier createHttpHeaderVerifier();

    public HttpBodyVerifier createHttpBodyVerifier(ResponseFields contractResponse, ProviderResponse providerResponse);
}
