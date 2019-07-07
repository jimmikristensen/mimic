package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.ProviderResponse;

import java.util.Map;

public class HttpHeaderVerifier {

    public Map<String, String> verify(ResponseFields contractResponseFields, ProviderResponse providerResponseFields) {
        assert contractResponseFields.getStatus() == providerResponseFields.getStatus() : "Oooops";
        return null;
    }
}
