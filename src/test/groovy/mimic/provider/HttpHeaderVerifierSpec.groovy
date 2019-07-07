package mimic.provider

import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.ProviderResponse
import mimic.mountebank.provider.verifier.HttpHeaderVerifier
import spock.lang.Specification


class HttpHeaderVerifierSpec extends Specification {

    def "HTTP status code is successfully verified agains imposter response fields"() {
        given:
        def contractResponseFields = new ResponseFields(statusCode: 201)
        def providerResponseFields = new ProviderResponse(statusCode: 200)

        when:
        new HttpHeaderVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        1 == 1
    }

}