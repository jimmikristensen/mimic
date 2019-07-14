package mimic.mountebank.provider.verifier.net.http;

import mimic.mountebank.imposter.HttpPredicate;
import mimic.mountebank.provider.ProviderResponse;

public interface HTTPClient {

    public ProviderResponse sendRequest(String baseUrl, HttpPredicate httpPredicate);
}
