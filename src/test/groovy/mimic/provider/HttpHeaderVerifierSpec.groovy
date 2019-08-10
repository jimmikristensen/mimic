package mimic.provider

import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.verifier.HttpHeaderVerifier
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult
import mimic.mountebank.provider.verifier.results.ReportStatus
import mimic.mountebank.provider.verifier.results.diff.Diff
import mimic.mountebank.provider.verifier.results.diff.DiffOperation
import mimic.mountebank.provider.verifier.results.diff.DiffSet
import spock.lang.Specification


class HttpHeaderVerifierSpec extends Specification {

    def "HTTP status code is successfully verified agains imposter response fields"() {
        given:
        DiffSet.clear()
        def contractResponseFields = new ResponseFields(statusCode: 201)
        def providerResponseFields = new ProviderHTTPResult(responseStatus: 201)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.getDiff().size() == 1
        verificationResult.getDiff().get(0).getOperation() == DiffOperation.EQUAL
    }

    def "difference in status between contract and provider results in unvarified"() {
        given:
        DiffSet.clear()
        def contractResponseFields = new ResponseFields(statusCode: 201)
        def providerResponseFields = new ProviderHTTPResult(responseStatus: 200)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() == 3

        and:
        diff.get(0).getOperation() == DiffOperation.EQUAL
        diff.get(0).getType() == Diff.Type.TEXT
        diff.get(0).getValue() == "20"

        and:
        diff.get(1).getOperation() == DiffOperation.REMOVE
        diff.get(1).getValue() == "0"

        and:
        diff.get(2).getOperation() == DiffOperation.ADD
        diff.get(2).getValue() == "1"
    }

    def "the exact same headers between contract and provider is verified"() {
        given:
        DiffSet.clear()
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
        def diff = verificationResult.getDiff()
        diff.size() == 1

        and:
        diff.get(0).getOperation() == DiffOperation.EQUAL
        diff.get(0).getValue() == "201"
    }

    def "the exact same headers, but in different order, between contract and provider is verified"() {
        given:
        DiffSet.clear()
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
        def diff = verificationResult.getDiff()
        diff.size() == 1

        and:
        diff.get(0).getOperation() == DiffOperation.EQUAL
        diff.get(0).getValue() == "201"
    }

    def "difference in headers between contract and provider is unverified"() {
        given:
        DiffSet.clear()
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
        def diff = verificationResult.getDiff()
        diff.size() == 2

        and:
        diff.get(1).getOperation() == DiffOperation.ADD
        diff.get(1).getPath() == "X-Header"
        diff.get(1).getValue() == "this is x"
    }

    def "difference in header keys but same amount between contract and provider is unverified"() {
        given:
        DiffSet.clear()
        def contractHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y",
                "F-Header":"this is f"
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
        def diff = verificationResult.getDiff()

        and:
        diff.get(1).getOperation() == DiffOperation.ADD
        diff.get(1).getPath() == "F-Header"
        diff.get(1).getValue() == "this is f"
    }

    def "difference in headers count between contract and provider is verified"() {
        given:
        DiffSet.clear()
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
        verificationResult.getDiff()
        verificationResult.getReportStatus() == ReportStatus.OK
    }

    def "difference in headers value between contract and provider is unverified"() {
        given:
        DiffSet.clear()
        def contractHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is not y"
        ]
        def providertHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y"
        ]
        def contractResponseFields = new ResponseFields(status: 201, headers: contractHeaders)
        def providerResponseFields = new ProviderHTTPResult(responseStatus:  201, responseHeaders: providertHeaders)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.get(0).getOperation() == DiffOperation.EQUAL
        diff.get(0).getValue() == '201'

        and:
        diff.get(1).getOperation() == DiffOperation.REPLACE
        diff.get(1).getValue() == 'this is not y'
        diff.get(1).getFrom() == 'this is y'
    }

    def "difference in header and status code between contract and provider"() {
        given:
        DiffSet.clear()
        def contractHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is not y"
        ]
        def providertHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y"
        ]
        def contractStatusCode = 201
        def providerStatusCode = 203
        def contractResponseFields = new ResponseFields(status: contractStatusCode, headers: contractHeaders)
        def providerResponseFields = new ProviderHTTPResult(responseStatus:  providerStatusCode, responseHeaders: providertHeaders)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.get(0).getOperation() == DiffOperation.EQUAL
        diff.get(0).getValue() == "20"
        diff.get(1).getOperation() == DiffOperation.REMOVE
        diff.get(1).getValue() == "3"
        diff.get(2).getOperation() == DiffOperation.ADD
        diff.get(2).getValue() == "1"

        and:
        diff.get(3).getOperation() == DiffOperation.REPLACE
        diff.get(3).path == "Y-Header"
        diff.get(3).getValue() == "this is not y"
        diff.get(3).getFrom() == "this is y"
    }

    def "difference in header value case between contract and provider will fail"() {
        given:
        DiffSet.clear()
        def contractHeaders = [
                "X-Header":"this is x"
        ]
        def providertHeaders = [
                "X-Header":"THIS IS X"
        ]
        def contractResponseFields = new ResponseFields(status: 201, headers: contractHeaders)
        def providerResponseFields = new ProviderHTTPResult(responseStatus:  201, responseHeaders: providertHeaders)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED

        and:
        def diff = verificationResult.getDiff()
        diff.get(1).getOperation() == DiffOperation.REPLACE
        diff.get(1).getPath() == "X-Header"
        diff.get(1).getValue() == "this is x"
        diff.get(1).getFrom() == "THIS IS X"
    }

    def "difference in header key case between contract and provider will fail"() {
        given:
        DiffSet.clear()
        def contractHeaders = [
                "X-Header":"this is x"
        ]
        def providertHeaders = [
                "X-HEADER":"this is x"
        ]
        def contractResponseFields = new ResponseFields(status: 201, headers: contractHeaders)
        def providerResponseFields = new ProviderHTTPResult(responseStatus:  201, responseHeaders: providertHeaders)

        when:
        def verificationResult = new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED

        and:
        def diff = verificationResult.getDiff()
        diff.get(1).getOperation() == DiffOperation.ADD
        diff.get(1).getPath() == "X-Header"
        diff.get(1).getValue() == "this is x"
    }
}