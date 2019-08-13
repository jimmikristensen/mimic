package mimic.provider

import mimic.mountebank.consumer.ConsumerImposterBuilder
import mimic.mountebank.provider.ProviderImposterVerifier
import mimic.mountebank.net.http.HttpMethod
import org.testcontainers.containers.GenericContainer
import spock.lang.Shared
import spock.lang.Specification


class ProviderImposterVerifierSpec extends Specification {

    @Shared
    GenericContainer container = ConsumerImposterBuilder.Builder()
            .givenRequest(4321)
            .equals()
            .path("/test")
            .method(HttpMethod.GET)
            .expectResponse()
            .status(201)
            .toMountebank()

    def "verifying local imposter is successful"() {
        when:
        def isVerified = ProviderImposterVerifier.Verify()

//        ProviderImposterVerifier.Builder()
//        .withContractFromClasspath()
//        .withContractFromFile()
//        .withContractFromMountebank()
//        .verify()

        // should this be a builder?

        then:
        isVerified == true
    }

}