package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.report.ProviderReport;
import mimic.mountebank.provider.verifier.net.http.HTTPClient;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;


public interface VerificationFactory {

    public MessageHeaderVerifier createHeaderVerifier();

    public MessageBodyVerifier createBodyVerifier(ResponseFields contractResponse, ProviderHTTPResult providerResponse);

    public HTTPClient createHttpClient();

    public ProviderReport createReport();
}
