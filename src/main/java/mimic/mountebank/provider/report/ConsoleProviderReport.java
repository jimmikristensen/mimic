package mimic.mountebank.provider.report;

import mimic.mountebank.provider.verifier.results.HttpHeaderVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;
import mimic.mountebank.provider.verifier.results.ReportStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public final class ConsoleProviderReport implements ProviderReport {

    private final ProviderHTTPResult httpResult;
    private final HttpHeaderVerificationResult httpHeaderResult;

    public ConsoleProviderReport(ProviderHTTPResult httpResult, HttpHeaderVerificationResult httpHeaderResult) {
        this.httpResult = httpResult;
        this.httpHeaderResult = httpHeaderResult;
    }

    @Override
    public void saveReport() {

    }

    @Override
    public void printReport() {
        System.out.println(getReport());
    }

    @Override
    public String getReport() {
        StringBuilder sb = new StringBuilder();
        createReportHeader(sb);

        if (httpResult != null) {
            createHttpReportPart(sb);
        }

        if (httpHeaderResult != null) {
            createHttpHeaderReportPart(sb);
        }

        return sb.toString();
    }

    private void createReportHeader(StringBuilder sb) {
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSSS");
        sb.append(String.format("+------Contract Verification Report-------+%n"));
        sb.append(String.format("| Generated at | %s |%n", formatter.format(new Date())));
        sb.append(String.format("+-----------------------------------------+%n%n"));
    }

    private void createHttpReportPart(StringBuilder sb) {
        sb.append(String.format("Sending %s request to provider using URL: ", httpResult.getHttpMethod()));
        sb.append(String.format("%s%n", httpResult.getRequestUrl()));

        if (!httpResult.getRequestHeaders().isEmpty()) {
            sb.append(String.format("  With headers:%n"));
            for (Map.Entry<String, String> entry : httpResult.getRequestHeaders().entrySet()) {
                sb.append(String.format("    %s=%s%n", entry.getKey(), entry.getValue()));
            }
        }

        if (httpResult.getRequestBody() != null && httpResult.getRequestBody() != "") {
            sb.append(String.format("  With body:%n    %s", httpResult.getRequestBody()));
        }

        sb.append(String.format("%n%nReceived response from provider:%n"));
        sb.append(String.format("  With status code: %s%n", httpResult.getResponseStatus()));

        if (httpResult.getResponseMediaType() != null && httpResult.getResponseMediaType() != "") {
            sb.append(String.format("  With media type: %s%n", httpResult.getResponseMediaType()));
        }

        if (!httpResult.getResponseHeaders().isEmpty()) {
            sb.append(String.format("  With headers:%n"));
            for (Map.Entry<String, String> entry : httpResult.getResponseHeaders().entrySet()) {
                sb.append(String.format("    %s=%s%n", entry.getKey(), entry.getValue()));
            }
        }
        if (httpResult.getRequestBody() != null && httpResult.getResponseBody() != "") {
            sb.append(String.format("  With body:%n    %s", httpResult.getRequestBody()));
        }
    }

    private void createHttpHeaderReportPart(StringBuilder sb) {
        sb.append(String.format("Verifying HTTP header between contract and provider%n"));
        sb.append(String.format("  Given a request to provider%n"));
        sb.append(String.format("    returns with response status %s%n", httpHeaderResult.getContractStatusCode()));

        if (httpHeaderResult.getContractHeaders()!= null && !httpHeaderResult.getContractHeaders().isEmpty()) {
            sb.append(String.format("    and includes the following headers:%n"));
            httpHeaderResult.getContractHeaders().forEach((k,v) -> {
                sb.append(String.format("      \"%s\" with value \"%s\"%n", k, v));
            });
        }

        if (httpHeaderResult.getReportStatus() == ReportStatus.OK) {
            sb.append(String.format("Verification (%s)%n", httpHeaderResult.getReportStatus()));
        } else {
            sb.append(String.format("Verification (%s)%n", httpHeaderResult.getReportStatus()));
            sb.append(String.format("  Failures:%n"));
            sb.append(String.format("    Failures:%n"));
        }


        httpHeaderResult.getContractHeaders();
        httpHeaderResult.getReportStatus();
    }
}
