package mimic.consumer

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.net.http.MountebankClient
import mimic.mountebank.MountebankContainerBuilder
import mimic.mountebank.imposter.Imposter
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import spock.lang.Shared
import spock.lang.Specification


class ConsumerImposterEqualsIntegrationSpec extends Specification {

    @Shared
    def mbContainer

    @Shared
    def stubUrl

    def setupSpec() {
        mbContainer = new MountebankContainerBuilder().managementPort(2525).stubPort(4321).build()
        stubUrl = "http://localhost:${mbContainer.getMappedPort(4321)}"
    }

    def setup() {
        new MountebankClient(mbContainer).deleteAllImposters()
    }

    def "posting simple imposter to mountebank is successful"() {
        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method(HttpMethod.POST)
                    .path("/test")
                    .query("q", "some query")
                    .header("Some-Header", "Header-Data")
                .expectResponse()
                    .status(201)
                .toImposterString()

        then:
        boolean isPosted = new MountebankClient(mbContainer).postImposter(impStr)
        isPosted == true

        and:
        Response resp = sendPostToStub("${stubUrl}/test?q=some query", ["Some-Header":"Header-Data"], "")
        resp.isSuccessful() == true
        resp.body().string() == ""
        resp.code() == 201

        and:
        println impStr
    }

    def "creating imposter stub with multiple queries succeeds"() {
        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method("POST")
                    .path("/test")
                    .query("q", "some query")
                    .query("q2", "some other query")
                    .query("q3", "some third query")
                    .header("Some-Header", "Header-Data")
                .expectResponse()
                    .status(201)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStub(0).getPredicate(0).getEquals().getQueries() == [
                "q":"some query",
                "q2":"some other query",
                "q3":"some third query"
        ]

        and:
        boolean isPosted = new MountebankClient(mbContainer).postImposter(impStr)
        isPosted == true

        and:
        Response resp = sendPostToStub("${stubUrl}/test?q=some query&q2=some other query&q3=some third query", ["Some-Header":"Header-Data"], "")
        resp.isSuccessful() == true
        resp.body().string() == ""
        resp.code() == 201

        and:
        println impStr
    }

    def "creating imposter stub with multiple headers succeeds"() {
        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method("POST")
                    .path("/test")
                    .query("q", "some query")
                    .header("Some-Header", "Header-Data")
                    .header("Some-Other-Header", "Header-Data2")
                    .header("Some-Third-Header", "Header-Data3")
                .expectResponse()
                    .status(201)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStub(0).getPredicate(0).getEquals().getHeaders() == [
                "Some-Header":"Header-Data",
                "Some-Other-Header":"Header-Data2",
                "Some-Third-Header":"Header-Data3"
        ]

        and:
        boolean isPosted = new MountebankClient(mbContainer).postImposter(impStr)
        isPosted == true

        and:
        Response resp = sendPostToStub("${stubUrl}/test?q=some query",
                ["Some-Header":"Header-Data", "Some-Other-Header":"Header-Data2", "Some-Third-Header":"Header-Data3"],
                "")
        resp.isSuccessful() == true
        resp.body().string() == ""
        resp.code() == 201

        and:
        println impStr
    }

    def "creating imposter stub with response status code, body and 3 headers is successful"() {
        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method("get")
                    .path("/test")
                .expectResponse()
                    .status(201)
                    .body("Hello World!")
                    .header("ResponseFields-Header1", "Header1")
                    .header("ResponseFields-header2", "Header2")
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStub(0).getResponses().get(0).getFields().getStatus() == 201
        imp.getStub(0).getResponses().get(0).getFields().getBody() == "Hello World!"
        imp.getStub(0).getResponses().get(0).getFields().getHeaders() == [
                "ResponseFields-Header1":"Header1",
                "ResponseFields-header2":"Header2"
        ]

        and:
        boolean isPosted = new MountebankClient(mbContainer).postImposter(impStr)
        isPosted == true

        and:
        Response resp = sendGetToStub("${stubUrl}/test")
        resp.isSuccessful() == true
        resp.body().string() == "Hello World!"
        resp.header("ResponseFields-Header1") == "Header1"
        resp.header("ResponseFields-header2") == "Header2"
        resp.code() == 201

        and:
        println impStr
    }

    def "using an JsonObject for response body will result in json string response"() {
        given:
        def mapper = new ObjectMapper()
        JsonNode rootNode = mapper.createObjectNode()
        JsonNode node = mapper.createObjectNode()
        ((ObjectNode) node).put("key1", "value1")
        ((ObjectNode) rootNode).set("test", node)

        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method("get")
                    .path("/test")
                .expectResponse()
                    .status(201)
                    .header("Content-Type", "application/json")
                    .body(rootNode)
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStub(0).getResponses().get(0).getFields().getStatus() == 201
        imp.getStub(0).getResponses().get(0).getFields().getBody() == mapper.writer().writeValueAsString(rootNode)

        and:
        boolean isPosted = new MountebankClient(mbContainer).postImposter(impStr)
        isPosted == true

        and:
        Response resp = sendGetToStub("${stubUrl}/test")
        resp.isSuccessful() == true
        resp.body().string() == mapper.writer().writeValueAsString(rootNode)
        resp.header("Content-Type") == "application/json"
        resp.code() == 201

        and:
        println impStr
    }

    def "creating imposter stub with two responses is successful"() {
        when:
        def impStr = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .method("get")
                    .path("/test")
                .expectResponse()
                    .status(200)
                    .body("Hello World!")
                .respondsWith()
                    .status(201)
                    .body("Hello Second World!")
                .toImposterString()

        then:
        Imposter imp = ConsumerImposterBuilder.getImposter()
        imp.getStub(0).getResponses().size() == 2
        imp.getStub(0).getResponse(0).getFields().getStatus() == 200
        imp.getStub(0).getResponse(1).getFields().getStatus() == 201

        and:
        boolean isPosted = new MountebankClient(mbContainer).postImposter(impStr)
        isPosted == true

        and:
        Response res0 = sendGetToStub("${stubUrl}/test")
        res0.body().string() == "Hello World!"

        and:
        Response res1 = sendGetToStub("${stubUrl}/test")
        res1.body().string() == "Hello Second World!"

        and:
        println impStr
    }

    def "imposter two equals predicates stacks the predicate validater"() {
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
                    .status(201)
                .toImposterString()

        then:
        boolean isPosted = new MountebankClient(mbContainer).postImposter(impStr)
        isPosted == true

        when:
        Response res0 = sendPostToStub("${stubUrl}/test?q=test", null, "Hello World!")
        res0.body().string() == "Hello World!"

        then:
        impStr != ""
        println impStr
    }

    private Response sendPostToStub(String url, Map<String, String> headersMap, String body) {
        Headers.Builder headersBuilder = new Headers.Builder()
        headersMap.each {k, v ->
            headersBuilder.add(k, v)
        }
        Headers headers = headersBuilder.build()

        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                body
        )

        OkHttpClient client = new OkHttpClient()
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build()

        return client.newCall(request).execute()
    }

    private Response sendGetToStub(String url) {
        OkHttpClient client = new OkHttpClient()
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build()

        return client.newCall(request).execute()
    }
}
