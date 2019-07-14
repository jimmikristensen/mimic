package mimic.provider

import mimic.mountebank.consumer.ConsumerImposterBuilder
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.provider.verifier.net.http.StandardHTTPClient
import spock.lang.Specification


class HTTPClientSpec extends Specification {

    def "using a predicate a GET request is sent to the provider server"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
            .givenRequest(4321)
                .equals()
                .method(HttpMethod.GET)
                .path("/test")
            .expectResponse()
                .status(201)
            .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def providerResponse = new StandardHTTPClient()
            .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        providerResponse.getStatus() == 201
        providerResponse.getHeaders() == [:]
        providerResponse.getBody() == ''
        providerResponse.getMediaType() == null

    }

}