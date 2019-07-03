package mimic

import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.ProducerImposterVerifier
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