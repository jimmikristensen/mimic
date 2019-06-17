package mimic

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.net.Protocol
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


class ConsumerImposterSpec extends Specification {

    @Shared
    def mountebankContainer

    @Shared
    def mountebankUrl

    @Shared
    def stubUrl

    def setupSpec() {
        mountebankContainer = new MountebankContainerBuilder().managementPort(2525).stubPort(4321).build()
        mountebankUrl = "http://localhost:${mountebankContainer.getMappedPort(2525)}"
        stubUrl = "http://localhost:${mountebankContainer.getMappedPort(4321)}"
    }

    def setup() {
        new MountebankClient().deleteAllImposters("${mountebankUrl}/imposters")
    }

    def "posting simple imposter to mountebank is successful"() {
        given:
        def cib = new ConsumerImposterBuilder(4321, Protocol.HTTP)
        def impostersUrl = "${mountebankUrl}/imposters"

        when:
        def predicate = cib
                .givenRequest()
                    .equals()
                    .method(HttpMethod.POST)
                    .path("/test")
                    .query("q", "some query")
                    .header("Some-Header", "Header-Data")
                .respondsWith()
                    .status(201)

        then:
        Imposter imp = cib.getImposter()
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
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), impostersUrl)
        isPosted == true

        and:
        new MountebankClient().getImposters(impostersUrl).size() == 1

        and:
        Response resp = sendPostToMountebank("${stubUrl}/test?q=some query", ["Some-Header":"Header-Data"], "")
        resp.isSuccessful() == true
        resp.body().string() == ""
        resp.code() == 201

        and:
        println cib.getImposterAsJsonString()
    }

    def "creating imposter stub with multiple queries succeeds"() {
        given:
        def cib = new ConsumerImposterBuilder(4321)
        def impostersUrl = "${mountebankUrl}/imposters"

        when:
        def predicate = cib
                .givenRequest()
                    .equals()
                    .method("POST")
                    .path("/test")
                    .query("q", "some query")
                    .query("p2", "some other query")
                    .query("p3", "some third query")
                    .header("Some-Header", "Header-Data")
                .respondsWith()
                    .status(200)
        then:
        Imposter imp = cib.getImposter()
        imp.getStub(0).getPredicate(0).getEquals().getQueries() == [
                "q":"some query",
                "p2":"some other query",
                "p3":"some third query"
        ]

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), impostersUrl)
        isPosted == true
        println cib.getImposterAsJsonString()
    }

    def "creating imposter stub with multiple headers succeeds"() {
        given:
        def cib = new ConsumerImposterBuilder(4321)
        def impostersUrl = "${mountebankUrl}/imposters"

        when:
        def predicate = cib
                .givenRequest()
                    .equals()
                    .method("POST")
                    .path("/test")
                    .query("q", "some query")
                    .header("Some-Header", "Header-Data")
                    .header("Some-Other-Header", "Header-Data2")
                    .header("Some-Third-Header", "Header-Data3")
                .respondsWith()
                    .status(200)
        then:
        Imposter imp = cib.getImposter()
        imp.getStub(0).getPredicate(0).getEquals().getHeaders() == [
                "Some-Header":"Header-Data",
                "Some-Other-Header":"Header-Data2",
                "Some-Third-Header":"Header-Data3"
        ]

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), impostersUrl)
        isPosted == true
        println cib.getImposterAsJsonString()
    }

    def "creating imposter stub with response status code, body and 3 headers is successful"() {
        given:
        def cib = new ConsumerImposterBuilder(4321)
        def impostersUrl = "${mountebankUrl}/imposters"

        when:
        def predicate = cib
                .givenRequest()
                    .equals()
                    .method("get")
                    .path("/test")
                .respondsWith()
                    .status(200)
                    .body("Hello World!")
                    .header("ResponseFields-Header1", "Header1")
                    .header("ResponseFields-header2", "Header2")
        then:
        Imposter imp = cib.getImposter()
        imp.getStub(0).getResponses().get(0).getFields().getStatus() == 200
        imp.getStub(0).getResponses().get(0).getFields().getBody() == "Hello World!"
        imp.getStub(0).getResponses().get(0).getFields().getHeaders() == [
                "ResponseFields-Header1":"Header1",
                "ResponseFields-header2":"Header2"
        ]

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), impostersUrl)
        isPosted == true
        println cib.getImposterAsJsonString()
    }

    def "using an JsonObject for response body will result in json string response"() {
        given:
        def cib = new ConsumerImposterBuilder(4321)
        def impostersUrl = "${mountebankUrl}/imposters"

        def mapper = new ObjectMapper()
        JsonNode rootNode = mapper.createObjectNode()
        JsonNode node = mapper.createObjectNode()
        ((ObjectNode) node).put("key1", "value1")
        ((ObjectNode) rootNode).set("test", node)

        when:
        def predicate = cib
                .givenRequest()
                    .equals()
                    .method("get")
                    .path("/test")
                .respondsWith()
                    .status(200)
                    .header("Content-Type", "application/json")
                    .body(rootNode)

        then:
        Imposter imp = cib.getImposter()
        imp.getStub(0).getResponses().get(0).getFields().getStatus() == 200
        imp.getStub(0).getResponses().get(0).getFields().getBody() == mapper.writer().writeValueAsString(rootNode)

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), impostersUrl)
        isPosted == true
        println cib.getImposterAsJsonString()
    }

    def "creating imposter stub with two responses is successful"() {
        given:
        def cib = new ConsumerImposterBuilder(4321)
        def impostersUrl = "${mountebankUrl}/imposters"

        when:
        def predicate = cib
                .givenRequest()
                    .equals()
                    .method("get")
                    .path("/test")
                .respondsWith()
                    .status(200)
                    .body("Hello World!")
                .respondsWith()
                    .status(201)
                    .body("Hello Second World!")

        then:
        Imposter imp = cib.getImposter()
        imp.getStub(0).getResponses().size() == 2
        imp.getStub(0).getResponse(0).getFields().getStatus() == 200
        imp.getStub(0).getResponse(1).getFields().getStatus() == 201

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), impostersUrl)
        isPosted == true
        println cib.getImposterAsJsonString()

        and:
        Response res0 = fetchImposterResponse("http://localhost:4321/test")
        res0.body().string() == "Hello World!"

        and:
        Response res1 = fetchImposterResponse("http://localhost:4321/test")
        res1.body().string() == "Hello Second World!"
    }

    private Response sendPostToMountebank(String url, Map<String, String> headersMap, String body) {
        Headers.Builder headersBuilder = new Headers.Builder()
        headersMap.each {k, v ->
            headersBuilder.add("Some-Header", "Header-Data")
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

    private Response fetchImposterResponse(String url) {
        OkHttpClient client = new OkHttpClient()
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build()

        return client.newCall(request).execute()
    }
}
