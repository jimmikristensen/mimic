package mimic.provider

import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.ProviderResponse
import mimic.mountebank.provider.verifier.HttpTextBodyVerifier
import spock.lang.Specification

class TextBodyVerifierSpec extends Specification {

    def "exact match between contract body and provider body is verified"() {
        given:
        def body = 'Some text'
        def contractResponseFields = new ResponseFields(body: body)
        def providerResponseFields = new ProviderResponse(201, null, [:], body)

        when:
        def isVerified = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        isVerified == true
    }

    def "mismatch between contract body and provider body is unverified"() {
        given:
        def contractBody = 'Some text'
        def providerBody = 'Some other body'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderResponse(201, null, [:], providerBody)

        when:
        def isVerified = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        isVerified == false
    }

    def "case mismatch between contract body and provider body is unverified"() {
        given:
        def contractBody = 'Some text'
        def providerBody = 'Some TEXT'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderResponse(201, null, [:], providerBody)

        when:
        def isVerified = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        isVerified == false
    }

    def "contract body is null but provider body contains a string is unverified"() {
        given:
        def contractBody = null
        def providerBody = 'Some TEXT'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderResponse(201, null, [:], providerBody)

        when:
        def isVerified = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        isVerified == false
    }

    def "contract body contains a string but provider body is null is unverified"() {
        given:
        def contractBody = 'Some TEXT'
        def providerBody = null
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderResponse(201, null, [:], providerBody)

        when:
        def isVerified = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        isVerified == false
    }

    def "both contract and provider body is null is verified"() {
        given:
        def contractBody = 'Some TEXT'
        def providerBody = null
        def contractResponseFields = new ResponseFields(body: contractBody)
        def providerResponseFields = new ProviderResponse(201, null, [:], providerBody)

        when:
        def isVerified = new HttpTextBodyVerifier().verify(contractResponseFields,providerResponseFields)

        then:
        isVerified == true
    }
}
