package mimic.provider

import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.provider.report.ConsoleProviderReport
import mimic.mountebank.provider.report.ProviderReportBuilder
import mimic.mountebank.provider.verifier.results.HttpHeaderVerificationResult
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult
import mimic.mountebank.provider.verifier.results.ReportStatus
import spock.lang.Specification

class ProviderReportBuilderSpec extends Specification {

    def "creating a ProviderReportBuilder builder and calling createConsoleReport returns a ConsoleProviderReport"() {
        given:
        def reportBuilder = new ProviderReportBuilder()

        when:
        def report = reportBuilder.createConsoleReport()

        then:
        report.class == ConsoleProviderReport.class
    }

    def "when given a provider http result and instructed to create console report, the report is printed to console"() {
        given:
        def httpResult = createHttpResult()

        expect:
        new ProviderReportBuilder().useProviderHttpResult(httpResult).createConsoleReport().printReport()

    }

    def "when given a provider OK header result and instructed to create console report, the report is printed to console"() {
        expect:
        new ProviderReportBuilder().useProviderHeaderResult(createOKHttpHeaderResult()).createConsoleReport().printReport()
    }

    def "when given a provider header result with different status and instructed to create console report, the report is printed to console"() {
        expect:
        new ProviderReportBuilder().useProviderHeaderResult(createFAILED1HttpHeaderResult()).createConsoleReport().printReport()
    }

    private ProviderHTTPResult createHttpResult() {
        def httpResult = new ProviderHTTPResult()

        httpResult.setHttpMethod(HttpMethod.GET)
        httpResult.setRequestUrl("http://someurl.com?test0=test&test1=test")
        httpResult.setRequestHeaders(["Head0":"Val0", "Head1":"Val1"])
        httpResult.setRequestBody('{"key0":"Val0","key1":"val1"}')

        httpResult.setResponseStatus(200)
        httpResult.setResponseMediaType("application/json")
        httpResult.setResponseHeaders(["Content-Type":"application/json", "X-Header":"some value"])
        httpResult.setResponseBody('{"content":"this is content"}')

        return httpResult
    }

    private HttpHeaderVerificationResult createOKHttpHeaderResult() {
        def httpHeaderResult = new HttpHeaderVerificationResult()
        httpHeaderResult.setReportStatus(ReportStatus.OK)
        httpHeaderResult.setContractStatusCode(204)
        httpHeaderResult.setContractHeaders(["Header0":"Value0", "Header1":"Value1"])
        httpHeaderResult.setProviderStatusCode(204)
        httpHeaderResult.setProviderHeaders(["Header0":"Value0", "Header1":"Value1"])

        return httpHeaderResult
    }

    private HttpHeaderVerificationResult createFAILED1HttpHeaderResult() {
        def httpHeaderResult = new HttpHeaderVerificationResult()
        httpHeaderResult.setReportStatus(ReportStatus.FAILED)
        httpHeaderResult.setContractStatusCode(204)
        httpHeaderResult.setContractHeaders(["Header0":"Value0", "Header1":"Value1"])
        httpHeaderResult.setProviderStatusCode(201)
        httpHeaderResult.setProviderHeaders(["Header0":"Value0", "Header1":"Value1"])

        return httpHeaderResult
    }
}
