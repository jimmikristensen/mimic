package mimic.provider

import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.verifier.HttpVerificationFactory
import mimic.mountebank.provider.verifier.HttpJsonBodyVerifier
import mimic.mountebank.provider.verifier.HttpHeaderVerifier
import mimic.mountebank.provider.verifier.HttpTextBodyVerifier
import spock.lang.Specification

class VerificationFactorySpec extends Specification {

    def "createHttpHeaderVerifier returns an StandardHttpHeaderVerifier object"() {
        given:
        def httpVerifier = new HttpVerificationFactory().createHeaderVerifier()

        expect:
        httpVerifier.getClass() == HttpHeaderVerifier.class
    }

    def "createHttpBodyVerifier returns an JsonBodyVerifier object if body looks like json"() {
        given:
        def contractBody = '{"key": "value"}'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def httpVerifier = new HttpVerificationFactory().createBodyVerifier(contractResponseFields, null)

        expect:
        httpVerifier.getClass() == HttpJsonBodyVerifier.class
    }

    def "createHttpBodyVerifier returns an TextBodyVerifier object if body doesn't look like json"() {
        given:
        def contractBody = 'This is regular text'
        def contractResponseFields = new ResponseFields(body: contractBody)
        def httpVerifier = new HttpVerificationFactory().createBodyVerifier(contractResponseFields, null)

        expect:
        httpVerifier.getClass() == HttpTextBodyVerifier.class
    }

    def "createHttpBodyVerifier returns an TextBodyVerifier object if body is null"() {
        given:
        def contractBody = null
        def contractResponseFields = new ResponseFields(body: contractBody)
        def httpVerifier = new HttpVerificationFactory().createBodyVerifier(contractResponseFields, null)

        expect:
        httpVerifier.getClass() == HttpTextBodyVerifier.class
    }
}
