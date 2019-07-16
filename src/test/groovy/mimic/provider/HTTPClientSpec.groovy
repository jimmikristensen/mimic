package mimic.provider

import mimic.mountebank.consumer.ConsumerImposterBuilder
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.provider.verifier.net.http.StandardHTTPClient
import mimic.mountebank.provider.verifier.results.HTTPResult
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
        def httpResult = new StandardHTTPClient()
            .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.GET
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test".toString()
        httpResult.getRequestHeaders() == [:]
        httpResult.requestBody == ""

        and:
        httpResult.getResponseStatus() == 201
        httpResult.getResponseBody() == ""
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
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.GET
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test".toString()
        httpResult.getRequestHeaders() == [:]
        httpResult.requestBody == ""

        and:
        httpResult.getResponseStatus() == 201
        httpResult.getResponseHeaders().get("Res-Header0") == "Value0"
        httpResult.getResponseHeaders().get("Res-Header1") == "Value1"
        httpResult.getResponseBody() == ""
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
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.GET
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test".toString()
        httpResult.getRequestHeaders() == [:]
        httpResult.requestBody == ""

        and:
        httpResult.getResponseStatus() == 201
        httpResult.getResponseHeaders().get("Res-Header0") == "Value0"
        httpResult.getResponseHeaders().get("Res-Header1") == "Value1"
        httpResult.getResponseBody() == "This is body content"
        httpResult.getResponseMediaType() == null
    }

    def "using a predicat, a GET request with request headers but plain status code as response"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.GET)
                    .header("Req-Header0", "Val0")
                    .header("Req-Header1", "val1")
                    .path("/test")
                .expectResponse()
                    .status(201)
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.GET
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test".toString()
        httpResult.getRequestHeaders().get("Req-Header0") == "Val0"
        httpResult.getRequestHeaders().get("Req-Header1") == "val1"
        httpResult.requestBody == ""

        and:
        httpResult.getResponseStatus() == 201
        httpResult.getResponseBody() == ""
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
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.POST
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test-post".toString()
        httpResult.requestBody == "This is request body"

        and:
        httpResult.getResponseStatus() == 201
        httpResult.getResponseBody() == ""

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

    def "using a predicate, a DELETE request is sent to the provider server"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.DELETE)
                    .path("/test-post/10")
                .expectResponse()
                    .status(202)
                .printImposter()
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.DELETE
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test-post/10".toString()
        httpResult.requestBody == ""

        and:
        httpResult.getResponseStatus() == 202
        httpResult.getResponseBody() == ""
    }

    def "using a predicate, a PATCH request is sent to the provider server with no body which results in IllegalArgumentException"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.PATCH)
                    .path("/test-post/11")
                .expectResponse()
                    .status(204)
                .printImposter()
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        IllegalArgumentException ex = thrown();
    }

    def "using a predicate, a PATCH request is sent to the provider server"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.PATCH)
                    .path("/test-post/11")
                    .body("this is the patch data")
                .expectResponse()
                    .status(204)
                .printImposter()
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.PATCH
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test-post/11".toString()
        httpResult.requestBody == "this is the patch data"

        and:
        httpResult.getResponseStatus() == 204
        httpResult.getResponseBody() == ""
    }

    def "using a predicate, a HEAD request is sent to the provider server"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.HEAD)
                    .path("/test-head")
                .expectResponse()
                    .status(204)
                .printImposter()
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.HEAD
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test-head".toString()
        httpResult.requestBody == ""

        and:
        httpResult.getResponseStatus() == 204
        httpResult.getResponseBody() == ""
    }

    def "using a predicate, a PUT request is sent to the provider server"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.PUT)
                    .path("/test-put/11")
                    .body("this is the patch data")
                .expectResponse()
                    .status(201)
                .printImposter()
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.PUT
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test-put/11".toString()
        httpResult.requestBody == "this is the patch data"

        and:
        httpResult.getResponseStatus() == 201
        httpResult.getResponseBody() == ""
    }

    def "using a predicate with base URL but no request path is successful"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.GET)
                .expectResponse()
                    .status(201)
                .printImposter()
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.GET
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/".toString()
        httpResult.requestBody == ""

        and:
        httpResult.getResponseStatus() == 201
        httpResult.getResponseBody() == ""
    }

    def "using a predicate with no base URL and no request path results in IllegalArgumentException"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.GET)
                .expectResponse()
                    .status(201)
                .printImposter()
                .toMountebank()
        def baseUrl = ""

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "Request URL must contain at leas a base URL"
    }

    def "using a predicate with null base URL and no request path results in IllegalArgumentException"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.GET)
                .expectResponse()
                    .status(201)
                .printImposter()
                .toMountebank()
        def baseUrl = null

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "Request URL must contain at leas a base URL"
    }

    def "using a predicate with null base URL but a request path results in IllegalArgumentException"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.GET)
                    .path("/test")
                .expectResponse()
                    .status(201)
                .printImposter()
                .toMountebank()
        def baseUrl = null

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "Request URL must contain at leas a base URL"
    }

    def "using a predicate with no method results in IllegalArgumentException"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .path("/test")
                .expectResponse()
                    .status(201)
                .printImposter()
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def providerResponse = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "Request HTTP Method must be specified"
    }

    def "using predicate with non-successful response status code is successful"() {
        setup:
        def providerServer = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .path("/test")
                    .method(HttpMethod.GET)
                .expectResponse()
                    .status(404)
                    .body("Content not found")
                .printImposter()
                .toMountebank()
        def baseUrl = "http://localhost:${providerServer.getMappedPort(4321)}".toString()

        when:
        def httpResult = new StandardHTTPClient()
                .sendRequest(baseUrl, ConsumerImposterBuilder.getImposter().getStub(0).getPredicate(0).getEquals())

        then:
        httpResult.getHttpMethod() == HttpMethod.GET
        httpResult.getRequestUrl() == "http://localhost:${providerServer.getMappedPort(4321)}/test".toString()
        httpResult.requestBody == ""

        and:
        httpResult.getResponseStatus() == 404
        httpResult.getResponseBody() == "Content not found"
    }
}