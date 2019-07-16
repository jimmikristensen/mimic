package mimic.mountebank.provider.verifier.net.http;

import mimic.mountebank.imposter.HttpPredicate;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;

public interface HTTPClient {
    public ProviderHTTPResult sendRequest(String baseUrl, HttpPredicate httpPredicate);
}
