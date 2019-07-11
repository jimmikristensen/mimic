package mimic.mountebank.provider.verifier.net.http;

import mimic.mountebank.imposter.HttpPredicate;

public interface HTTPClient {

    public void sendRequest(String baseUrl, HttpPredicate httpPredicate);
}
