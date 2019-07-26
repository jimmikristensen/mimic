package mimic.provider

import mimic.mountebank.provider.verifier.results.HttpHeaderVerificationResult
import mimic.mountebank.provider.verifier.results.diff.DiffOperation
import mimic.mountebank.provider.verifier.results.diff.DiffSet
import spock.lang.Specification


class HttpHeaderVerificationResultSpec extends Specification {

    def "header missing on provider side"() {
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
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractHeaders(contractHeaders)
        result.setProviderHeaders(providertHeaders)

        then:
        def diff = result.getDiff()
        diff.get(1).getOperation() == DiffOperation.ADD
        diff.get(1).getPath() == "F-Header"
        diff.get(1).getValue() == "this is f"
    }

    def "only headers on the provider side"() {
        given:
        DiffSet.clear()
        def contractHeaders = [:]
        def providertHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y",
                "Z-Header":"this is z"
        ]
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractHeaders(contractHeaders)
        result.setProviderHeaders(providertHeaders)

        then:
        def diff = result.getDiff()
        diff.size() == 1
    }

    def "only headers on the contract side"() {
        given:
        DiffSet.clear()
        def contractHeaders = [
                "X-Header":"this is x",
                "Y-Header":"this is y",
                "Z-Header":"this is z"
        ]
        def providertHeaders = [:]
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractHeaders(contractHeaders)
        result.setProviderHeaders(providertHeaders)

        then:
        def diff = result.getDiff()
        diff.size() == 4

        and:
        diff.get(1).getOperation() == DiffOperation.ADD
        diff.get(1).getPath() == "X-Header"
        diff.get(1).getValue() == "this is x"

        and:
        diff.get(2).getOperation() == DiffOperation.ADD
        diff.get(2).getPath() == "Y-Header"
        diff.get(2).getValue() == "this is y"

        and:
        diff.get(3).getOperation() == DiffOperation.ADD
        diff.get(3).getPath() == "Z-Header"
        diff.get(3).getValue() == "this is z"
    }

    def "no headers on either side"() {
        given:
        DiffSet.clear()
        def contractHeaders = [:]
        def providertHeaders = [:]
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractHeaders(contractHeaders)
        result.setProviderHeaders(providertHeaders)

        then:
        def diff = result.getDiff()
        diff.size() == 1
    }

    def "provider header value differ from contract header value"() {
        given:
        DiffSet.clear()
        def contractHeaders = [
                "X-Header":"this is x"
        ]
        def providertHeaders = [
                "X-Header":"this is z"
        ]
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractHeaders(contractHeaders)
        result.setProviderHeaders(providertHeaders)

        then:
        def diff = result.getDiff()
        diff.get(1).getOperation() == DiffOperation.REPLACE
        diff.get(1).getPath() == "X-Header"
        diff.get(1).getValue() == "this is x"
        diff.get(1).getFrom() == "this is z"
    }

    def "contract headers are null or not set"() {
        given:
        DiffSet.clear()
        def contractHeaders = null
        def providertHeaders = [
                "X-Header":"this is z"
        ]
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractHeaders(contractHeaders)
        result.setProviderHeaders(providertHeaders)

        then:
        def diff = result.getDiff()
        diff.size() == 1
    }

    def "provider headers are null or not set"() {
        given:
        DiffSet.clear()
        def contractHeaders = [
                "X-Header":"this is z"
        ]
        def providertHeaders = null
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractHeaders(contractHeaders)
        result.setProviderHeaders(providertHeaders)

        then:
        def diff = result.getDiff()
        diff.size() == 2

        and:
        diff.get(1).getOperation() == DiffOperation.ADD
        diff.get(1).getPath() == "X-Header"
        diff.get(1).getValue() == "this is z"
    }

    def "status code are the same on both sides"() {
        given:
        DiffSet.clear()
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractStatusCode(201)
        result.setProviderStatusCode(201)

        then:
        def diff = result.getDiff()
        diff.size() == 1

        and:
        diff.get(0).getOperation() == DiffOperation.EQUAL
    }

    def "status code is different on provider side"() {
        given:
        DiffSet.clear()
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractStatusCode(201)
        result.setProviderStatusCode(404)

        then:
        def diff = result.getDiff()
        diff.size() == 2

        and:
        diff.get(0).getOperation() == DiffOperation.REMOVE
        diff.get(0).getValue() == "404"

        and:
        diff.get(1).getOperation() == DiffOperation.ADD
        diff.get(1).getValue() == "201"
    }

    def "status code is different on provider side but still within success status range"() {
        given:
        DiffSet.clear()
        def result = new HttpHeaderVerificationResult()

        when:
        result.setContractStatusCode(201)
        result.setProviderStatusCode(200)

        then:
        def diff = result.getDiff()
        diff.size() == 3

        and:
        diff.get(0).getOperation() == DiffOperation.EQUAL
        diff.get(0).getValue() == "20"

        and:
        diff.get(1).getOperation() == DiffOperation.REMOVE
        diff.get(1).getValue() == "0"

        and:
        diff.get(2).getOperation() == DiffOperation.ADD
        diff.get(2).getValue() == "1"
    }
}