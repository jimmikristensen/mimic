package mimic.producer

import mimic.mountebank.consumer.ConsumerImposterBuilder
import mimic.mountebank.producer.ProducerImposterVerifier
import mimic.mountebank.net.http.HttpMethod
import org.testcontainers.containers.GenericContainer
import spock.lang.Shared
import spock.lang.Specification


class ProducerImposterVerifierSpec extends Specification {

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
        def isVerified = ProducerImposterVerifier.Verify()

        then:
        isVerified == true
    }

}