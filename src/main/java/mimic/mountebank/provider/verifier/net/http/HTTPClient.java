package mimic.mountebank.provider.verifier.net.http;

import mimic.mountebank.imposter.HttpPredicate;
import mimic.mountebank.provider.verifier.results.HTTPResult;

public interface HTTPClient {
    public HTTPResult sendRequest(String baseUrl, HttpPredicate httpPredicate);
}
