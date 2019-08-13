package mimic.provider

import mimic.mountebank.consumer.ConsumerImposterBuilder
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.provider.verifier.HttpVerificationFactory
import mimic.mountebank.provider.verifier.ContractVerifier
import spock.lang.Specification


class ContractVerifierSpec extends Specification {

    def "verifying contract with matching status code succeeds"() {
        given:
        def providerServer = ConsumerImposterBuilder.Builder()
            .givenRequest(4321)
                .equals()
                .path("/test1")
                .query("test","yes")
                .header("Head1", "Val1")
                .header("Head2", "Val2")
                .method(HttpMethod.GET)
            .expectResponse()
                .status(201)
            .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def imposter = ConsumerImposterBuilder.getImposter()
        def contractVerifier = new ContractVerifier(new HttpVerificationFactory())

        and:
        boolean isContractVerified = contractVerifier.verify(baseUrl, imposter)

        then:
        isContractVerified == true
    }

}