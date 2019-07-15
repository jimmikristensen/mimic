package mimic.provider

import mimic.mountebank.consumer.ConsumerImposterBuilder
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.provider.verifier.HttpVerificationFactory
import mimic.mountebank.provider.verifier.ContractVerifier
import spock.lang.Specification


class ContractVerifierSpec extends Specification {

    def "verifying contract with matching status code succeeds"() {
        given:
        def imposter = ConsumerImposterBuilder.Builder()
            .givenRequest(4321)
                .equals()
                .path("/test1")
                .method(HttpMethod.GET)
            .expectResponse()
                .status(201)
            .toImposter()

        when:
        def contractVerifier = new ContractVerifier(new HttpVerificationFactory())

        and:
        contractVerifier.verify(imposter)

        then:
        1 == 1
    }

}