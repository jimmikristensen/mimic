package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.provider.ProviderResponse;

import java.io.IOException;

public class HttpContractVerificationFactory implements ContractVerificationFactory {

    @Override
    public HttpHeaderVerifier createHttpHeaderVerifier() {
        return new StandardHttpHeaderVerifier();
    }

    @Override
    public HttpBodyVerifier createHttpBodyVerifier(ResponseFields contractResponse, ProviderResponse providerResponse) {
        boolean isJson = false;

        try {
            // check if this should be a json body verifier by trying to parse the body
            JacksonObjectMapper.getMapper().readTree(contractResponse.getBody());
            isJson = true;
        } catch (IOException e) {
            // silence exception
        }

        return isJson ? new JsonBodyVerifier() : new TextBodyVerifier();
    }
}
