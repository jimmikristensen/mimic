package mimic.provider

import mimic.mountebank.consumer.ConsumerImposterBuilder
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.provider.verifier.net.http.StandardHTTPClient
import spock.lang.Specification


class HTTPClientSpec extends Specification {

    def "using a predicate, a GET request is sent to the provider server"() {
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
        providerResponse.getBody() == ''
        providerResponse.getMediaType() == null
    }

    def "using a predicate, a GET request with response response headers is sent to the provider server"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.GET)
                    .path("/test")
                .expectResponse()
                    .status(201)
                    .header("Res-Header0", "Value0")
                    .header("Res-Header1", "Value1")
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        providerResponse.getStatus() == 201
        providerResponse.getHeaders().get("Res-Header0") == "Value0"
        providerResponse.getHeaders().get("Res-Header1") == "Value1"
        providerResponse.getBody() == ''
        providerResponse.getMediaType() == null
    }

    def "using a predicate, a GET request with response headers and body is sent to the provider server"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.GET)
                    .path("/test")
                .expectResponse()
                    .status(201)
                    .header("Res-Header0", "Value0")
                    .header("Res-Header1", "Value1")
                    .body("This is body content")
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        providerResponse.getStatus() == 201
        providerResponse.getHeaders().get("Res-Header0") == "Value0"
        providerResponse.getHeaders().get("Res-Header1") == "Value1"
        providerResponse.getBody() == 'This is body content'
        providerResponse.getMediaType() == null
    }

    def "using a predicat, a GET request with request headers but plain status code as response"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.GET)
                    .header("Req-Header0", "Val0")
                    .header("Req.Header1", "val1")
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
        providerResponse.getMediaType() == null
    }

    def "using a predicate, a POST request is sent to the provider server"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.POST)
                    .path("/test-post")
                    .body("This is request body")
                .expectResponse()
                    .status(201)
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        providerResponse.getStatus() == 201
        providerResponse.getBody() == ''
        providerResponse.getMediaType() == null
    }

    def "using a predicate that defines a POST without a body results in a IllegalArgumentException"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.POST)
                    .path("/test-post")
                .expectResponse()
                    .status(201)
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        IllegalArgumentException ex = thrown()
    }
}