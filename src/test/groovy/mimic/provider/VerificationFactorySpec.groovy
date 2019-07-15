package mimic.provider

import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.verifier.HttpVerificationFactory
import mimic.mountebank.provider.verifier.JsonBodyVerifier
import mimic.mountebank.provider.verifier.StandardHttpHeaderVerifier
import mimic.mountebank.provider.verifier.TextBodyVerifier
import spock.lang.Specification

class ContractVerificationFactory extends Specification {

    def "createHttpHeaderVerifier returns an StandardHttpHeaderVerifier object"() {
        given:
        def httpVerifier = new HttpVerificationFactory().createHttpHeaderVerifier()

        expect:
        httpVerifier.getClass() == StandardHttpHeaderVerifier.class
    }

    def "createHttpBodyVerifier returns an JsonBodyVerifier object if body looks like json"() {
        given:
        def contractBody = '{"key": "value"}'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def httpVerifier = new HttpVerificationFactory().createHttpBodyVerifier(contractResponseFields, null)

        expect:
        httpVerifier.getClass() == JsonBodyVerifier.class
    }

    def "createHttpBodyVerifier returns an TextBodyVerifier object if body doesn't look like json"() {
        given:
        def contractBody = 'This is regular text'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def httpVerifier = new HttpVerificationFactory().createHttpBodyVerifier(contractResponseFields, null)

        expect:
        httpVerifier.getClass() == TextBodyVerifier.class
    }
}
