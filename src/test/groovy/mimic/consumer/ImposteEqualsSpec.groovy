package mimic.consumer

import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.imposter.Imposter
import mimic.mountebank.net.Protocol
import mimic.mountebank.net.http.HttpMethod
import spock.lang.Specification


class ImposteEqualsSpec extends Specification {


    def "creating imposter with method results in imposter with status code #statucCode"() {
        given:
        def statucCode = 201

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.POST)
                .expectResponse()
                    .status(statucCode)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 1
        imp.getStub(0).getPredicate(0).getEquals().getMethod() == HttpMethod.POST
        imp.getStub(0).getResponse(0).getFields().getStatus() == 201

        and:
        impStr != ""
        println impStr
    }

    def "creating imposter with method, path results in imposter with status code #statucCode"() {
        given:
        def statucCode = 201

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                .equals()
                    .method(HttpMethod.POST)
                    .path("/test")
                .expectResponse()
                    .status(statucCode)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 1
        imp.getStub(0).getPredicate(0).getEquals().getMethod() == HttpMethod.POST
        imp.getStub(0).getPredicate(0).getEquals().getPath() == "/test"
        imp.getStub(0).getResponse(0).getFields().getStatus() == 201

        and:
        impStr != ""
        println impStr
    }

    def "creating imposter with method, path, single query results in imposter with status code #statucCode"() {
        given:
        def statucCode = 201

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                .equals()
                    .method(HttpMethod.POST)
                    .path("/test")
                    .query("q", "some query")
                .expectResponse()
                    .status(statucCode)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 1
        imp.getStub(0).getPredicate(0).getEquals().getMethod() == HttpMethod.POST
        imp.getStub(0).getPredicate(0).getEquals().getPath() == "/test"
        imp.getStub(0).getPredicate(0).getEquals().getQueries() == ["q":"some query"]
        imp.getStub(0).getResponse(0).getFields().getStatus() == 201

        and:
        impStr != ""
        println impStr
    }

    def "creating imposter with method, path, single query, single header results in imposter with status code #statucCode"() {
        given:
        def statucCode = 201

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.POST)
                    .path("/test")
                    .query("q", "some query")
                    .header("Some-Header", "Header-Data")
                .expectResponse()
                    .status(statucCode)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 1
        imp.getStub(0).getPredicate(0).getEquals().getMethod() == HttpMethod.POST
        imp.getStub(0).getPredicate(0).getEquals().getPath() == "/test"
        imp.getStub(0).getPredicate(0).getEquals().getHeaders() == ["Some-Header":"Header-Data"]
        imp.getStub(0).getPredicate(0).getEquals().getQueries() == ["q":"some query"]
        imp.getStub(0).getResponse(0).getFields().getStatus() == 201

        and:
        impStr != ""
        println impStr
    }

    def "creating imposter with method, path, multiple queries, single header results in imposter with status code #statucCode"() {
        given:
        def statucCode = 201

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.POST)
                    .path("/test")
                    .query("q", "some query")
                    .query("q2", "some other query")
                    .header("Some-Header", "Header-Data")
                .expectResponse()
                    .status(statucCode)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 1
        imp.getStub(0).getPredicate(0).getEquals().getMethod() == HttpMethod.POST
        imp.getStub(0).getPredicate(0).getEquals().getPath() == "/test"
        imp.getStub(0).getPredicate(0).getEquals().getHeaders() == ["Some-Header":"Header-Data"]
        imp.getStub(0).getPredicate(0).getEquals().getQueries() == ["q":"some query", "q2":"some other query"]
        imp.getStub(0).getResponse(0).getFields().getStatus() == 201

        and:
        impStr != ""
        println impStr
    }

    def "creating imposter with method, path, multiple queries, multiple headers results in imposter with status code #statucCode"() {
        given:
        def statucCode = 201

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.POST)
                    .path("/test")
                    .query("q", "some query")
                    .query("q2", "some other query")
                    .header("Some-Header", "Header-Data")
                    .header("Some-Other-Header", "Header-Data2")
                .expectResponse()
                    .status(statucCode)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 1
        imp.getStub(0).getPredicate(0).getEquals().getMethod() == HttpMethod.POST
        imp.getStub(0).getPredicate(0).getEquals().getPath() == "/test"
        imp.getStub(0).getPredicate(0).getEquals().getHeaders() == ["Some-Header":"Header-Data", "Some-Other-Header":"Header-Data2"]
        imp.getStub(0).getPredicate(0).getEquals().getQueries() == ["q":"some query", "q2":"some other query"]
        imp.getStub(0).getResponse(0).getFields().getStatus() == 201

        and:
        impStr != ""
        println impStr
    }

    def "creating imposter with multiple responses results in imposter with status code #statucCode1 and #statucCode2"() {
        given:
        def statucCode1 = 200
        def statucCode2 = 401

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.POST)
                    .path("/test")
                .expectResponse()
                    .status(statucCode1)
                .respondsWith()
                    .status(statucCode2)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 1
        imp.getStub(0).getPredicate(0).getEquals().getMethod() == HttpMethod.POST
        imp.getStub(0).getPredicate(0).getEquals().getPath() == "/test"

        and:
        imp.getStub(0).getResponses().size() == 2
        imp.getStub(0).getResponse(0).getFields().getStatus() == 200
        imp.getStub(0).getResponse(1).getFields().getStatus() == 401

        and:
        impStr != ""
        println impStr
    }

    def "imposter two equals predicates stacks the predicate validater"() {
        given:
        def statusCode = 201

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .path("/test")
                    .query("q", "test")
                    .method(HttpMethod.POST)
                    .and()
                    .body("Hello World!")
                .expectResponse()
                    .status(statusCode)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 2
        imp.getStub(0).getPredicate(0).getEquals().getMethod() == HttpMethod.POST
        imp.getStub(0).getPredicate(0).getEquals().getPath() == "/test"
        imp.getStub(0).getPredicate(0).getEquals().getQueries() == ["q":"test"]

        and:
        imp.getStub(0).getPredicate(1).getEquals().getBody() == "Hello World!"

        and:
        impStr != ""
        println impStr
    }

}