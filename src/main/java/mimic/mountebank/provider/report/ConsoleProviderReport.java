package mimic.mountebank.provider.report;

import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public final class ConsoleProviderReport implements ProviderReport {

    private final ProviderHTTPResult httpResult;

    public ConsoleProviderReport(ProviderHTTPResult httpResult) {
        this.httpResult = httpResult;
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


        return sb.toString();
    }

    private void createReportHeader(StringBuilder sb) {
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSSS");
        sb.append(String.format("+------Contract Verification Report-------+%n"));
        sb.append(String.format("| Generated at | %s |%n", formatter.format(new Date())));
        sb.append(String.format("+-----------------------------------------+%n%n"));
    }
}
