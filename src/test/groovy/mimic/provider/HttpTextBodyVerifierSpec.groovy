package mimic.provider

import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.verifier.HttpTextBodyVerifier
import mimic.mountebank.provider.verifier.results.diff.DiffOperation
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult
import mimic.mountebank.provider.verifier.results.ReportStatus
import spock.lang.Specification

class HttpTextBodyVerifierSpec extends Specification {

    def "exact match between contract body and provider body is verified"() {
        given:
        def body = 'Some text'
        def contractResponseFields = new ResponseFields(body: body)
        def providerResponseFields = new ProviderHTTPResult(responseBody: body)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
        def diff = verificationResult.getDiff()
        diff.size() == 1
        diff.get(0).getOperation() == DiffOperation.EQUAL
    }

    def "mismatch between contract body and provider body is unverified"() {
        given:
        def contractBody = 'Some text'
        def providerBody = 'Some other body'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerBody)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() == 3
        diff.get(0).getOperation() == DiffOperation.EQUAL
        diff.get(0).getValue() == "Some "
        diff.get(1).getOperation() == DiffOperation.REMOVE
        diff.get(1).getValue() == "other body"
        diff.get(2).getOperation() == DiffOperation.ADD
        diff.get(2).getValue() == "text"
    }

    def "case mismatch between contract body and provider body is unverified"() {
        given:
        def contractBody = 'Some text'
        def providerBody = 'Some TEXT'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerBody)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() == 3
        diff.get(0).getOperation() == DiffOperation.EQUAL
        diff.get(0).getValue() == "Some "
        diff.get(1).getOperation() == DiffOperation.REMOVE
        diff.get(1).getValue() == "TEXT"
        diff.get(2).getOperation() == DiffOperation.ADD
        diff.get(2).getValue() == "text"
    }

    def "contract body is null but provider body contains a string is unverified"() {
        given:
        def contractBody = null
        def providerBody = 'Some TEXT'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerBody)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() ==1
        diff.get(0).getOperation() == DiffOperation.REMOVE
        diff.get(0).getValue() == "Some TEXT"
    }

    def "contract body contains a string but provider body is null is unverified"() {
        given:
        def contractBody = 'Some TEXT'
        def providerBody = null
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerBody)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() ==1
        diff.get(0).getOperation() == DiffOperation.ADD
        diff.get(0).getValue() == "Some TEXT"
    }

    def "both contract and provider body is null is verified"() {
        given:
        def contractBody = null
        def providerBody = null
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerBody)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        println verificationResult.getDiff()
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.getDiff().size() == 0
    }

    def "contract body is empty but provider body contains a string is unverified"() {
        given:
        def contractBody = ""
        def providerBody = 'Some TEXT'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerBody)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() ==1
        diff.get(0).getOperation() == DiffOperation.REMOVE
        diff.get(0).getValue() == "Some TEXT"
    }

    def "contract body contains a string but provider body is empty is unverified"() {
        given:
        def contractBody = 'Some TEXT'
        def providerBody = ""
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerBody)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() ==1
        diff.get(0).getOperation() == DiffOperation.ADD
        diff.get(0).getValue() == "Some TEXT"
    }

    def "both contract and provider body is empty is verified"() {
        given:
        def contractBody = ""
        def providerBody = ""
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerBody)

        when:
        def verificationResult = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        println verificationResult.getDiff()
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.getDiff().size() == 0
    }
}
