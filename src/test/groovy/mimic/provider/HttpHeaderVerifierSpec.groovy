package mimic.provider

import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.ProviderResponse
import mimic.mountebank.provider.verifier.StandardHttpHeaderVerifier
import spock.lang.Specification


class HttpHeaderVerifierSpec extends Specification {

    def "HTTP status code is successfully verified agains imposter response fields"() {
        given:
        def contractResponseFields = new ResponseFields(statusCode: 201)
        def providerResponseFields = new ProviderResponse(statusCode: 201)

        when:
        def isVerified = new StandardHttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == true
    }

    def "difference in status between contract and provider results in unvarified"() {
        given:
        def contractResponseFields = new ResponseFields(statusCode: 201)
        def providerResponseFields = new ProviderResponse(statusCode: 200)

        when:
        def isVerified = new StandardHttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == false
    }

    def "the exact same headers between contract and provider is verified"() {
        given:
        def headers = [
                "X-Header":"this is x",
                "Y-Header":"this is y"
        ]
        def contractResponseFields = new ResponseFields(headers: headers)
        def providerResponseFields = new ProviderResponse(headers: headers)

        when:
        def isVerified = new StandardHttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == true
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
        def contractResponseFields = new ResponseFields(headers: contractHeaders)
        def providerResponseFields = new ProviderResponse(headers: providertHeaders)

        when:
        def isVerified = new StandardHttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == true
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
        def contractResponseFields = new ResponseFields(headers: contractHeaders)
        def providerResponseFields = new ProviderResponse(headers: providertHeaders)

        when:
        def isVerified = new StandardHttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == false
    }

}