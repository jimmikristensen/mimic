package mimic.mountebank.provider.report;

import mimic.mountebank.provider.verifier.results.HttpHeaderVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;


public class ProviderReportBuilder {

    private ProviderHTTPResult httpResult;
    private HttpHeaderVerificationResult httpHeaderResult;

    public ProviderReportBuilder useProviderHttpResult(ProviderHTTPResult httpResult) {
        this.httpResult = httpResult;
        return this;
    }

    public ProviderReportBuilder useProviderHeaderResult(HttpHeaderVerificationResult httpHeaderResult) {
        this.httpHeaderResult = httpHeaderResult;
        return this;
    }

    public ProviderReport createConsoleReport() {
        return new ConsoleProviderReport(httpResult, httpHeaderResult);
    }

}
