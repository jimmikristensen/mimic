package mimic.mountebank.provider.report;

import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;


public class ProviderReportBuilder {

    private ProviderHTTPResult httpResult;

    public ProviderReportBuilder useProviderHttpResult(ProviderHTTPResult httpResult) {
        this.httpResult = httpResult;
        return this;
    }

    public ProviderReport createConsoleReport() {
        return new ConsoleProviderReport(httpResult);
    }
}
