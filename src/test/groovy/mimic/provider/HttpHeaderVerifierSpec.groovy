package mimic.provider

import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.verifier.HttpHeaderVerifier
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult
import mimic.mountebank.provider.verifier.results.ReportStatus
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
import spock.lang.Specification


class HttpHeaderVerifierSpec extends Specification {

    def "HTTP status code is successfully verified agains imposter response fields"() {
        given:
        def contractResponseFields = new ResponseFields(statusCode: 201)
        def providerResponseFields = new ProviderHTTPResult(responseStatus: 201)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.statusDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
    }

    def "difference in status between contract and provider results in unvarified"() {
        given:
        def contractResponseFields = new ResponseFields(statusCode: 201)
        def providerResponseFields = new ProviderHTTPResult(responseStatus: 200)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        verificationResult.statusDiff().get(1).operation == DiffMatchPatch.Operation.DELETE
    }

    def "the exact same headers between contract and provider is verified"() {
        given:
        def headers = [
                "X-Header":"this is x",
                "Y-Header":"this is y"
        ]
        def contractResponseFields = new ResponseFields(status: 201, headers: headers)
        def providerResponseFields = new ProviderHTTPResult(responseStatus:  201, responseHeaders: headers)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.statusDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
        verificationResult.headerDiff().size() == 0
    }

    def "the exact same headers, but in different order, between contract and provider is verified"() {
        given:
        def contractHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y"
        ]
        def providertHeaders = [
                "Y-Header":"this is y",
                "X-Header":"this is x"
        ]
        def contractResponseFields = new ResponseFields(status: 201, headers: contractHeaders)
        def providerResponseFields = new ProviderHTTPResult(responseStatus:  201, responseHeaders: providertHeaders)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.statusDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
        verificationResult.headerDiff().size() == 0
    }

    def "difference in headers between contract and provider is unverified"() {
        given:
        def contractHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y"
        ]
        def providertHeaders = [
                "Z-Header":"this is z",
                "Y-Header":"this is y"
        ]
        def contractResponseFields = new ResponseFields(status: 201, headers: contractHeaders)
        def providerResponseFields = new ProviderHTTPResult(responseStatus:  201, responseHeaders: providertHeaders)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        verificationResult.statusDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
        verificationResult.headerDiff().size() == 1
        verificationResult.headerDiff().get(0) == "X-Header"
    }

    def "difference in headers count between contract and provider is verified"() {
        given:
        def contractHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y"
        ]
        def providertHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y",
                "Z-Header":"this is z"
        ]
        def contractResponseFields = new ResponseFields(status: 201, headers: contractHeaders)
        def providerResponseFields = new ProviderHTTPResult(responseStatus:  201, responseHeaders: providertHeaders)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        verificationResult.getDiff()
    }

}