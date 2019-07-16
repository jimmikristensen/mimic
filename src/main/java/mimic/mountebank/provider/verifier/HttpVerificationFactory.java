package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.provider.ProviderResponse;
import mimic.mountebank.provider.report.ProviderReport;
import mimic.mountebank.provider.report.ProviderReportBuilder;
import mimic.mountebank.provider.verifier.net.http.HTTPClient;
import mimic.mountebank.provider.verifier.net.http.StandardHTTPClient;

import java.io.IOException;

public class HttpVerificationFactory implements VerificationFactory {

    private ProviderReportBuilder reportBuilder;

    public HttpVerificationFactory() {
        reportBuilder = new ProviderReportBuilder();
    }

    @Override
    public MessageHeaderVerifier createHeaderVerifier() {
        return new HttpHeaderVerifier();
    }

    @Override
    public MessageBodyVerifier createBodyVerifier(ResponseFields contractResponse, ProviderResponse providerResponse) {
        boolean isJson = false;

        if (contractResponse.getBody() != null) {
            try {
                // check if this should be a json body verifier by trying to parse the body
                JacksonObjectMapper.getMapper().readTree(contractResponse.getBody());
                isJson = true;
            } catch (IOException e) {
                // silence exception
            }
        }

        return isJson ? new HttpJsonBodyVerifier() : new HttpTextBodyVerifier();
    }

    @Override
    public HTTPClient createHttpClient() {
        return new StandardHTTPClient(null);
    }

    @Override
    public ProviderReport createReport() {
        return reportBuilder.build();
    }
}
