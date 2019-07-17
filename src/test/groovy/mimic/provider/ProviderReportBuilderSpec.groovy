package mimic.provider

import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.provider.report.ProviderReportBuilder
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult
import spock.lang.Specification

class ProviderReportBuilderSpec extends Specification {

    def "when given a provider http result and instructed to create console report, the report is printed to console"() {
        given:
        def httpResult = createHttpResult()

        expect:
        new ProviderReportBuilder().useProviderHttpResult(httpResult).createConsoleReport().printReport()

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
}
