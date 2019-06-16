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
import okhttp3.internal.http.HttpHeaders
import spock.lang.Shared
import spock.lang.Specification


class ConsumerImposterSpec extends Specification {

    @Shared
    def mountebankContainer

    @Shared
    def mountebankUrl

    def setupSpec() {
        mountebankContainer = new MountebankContainerBuilder().managementPort(2525).build()
        mountebankUrl = "http://localhost:${mountebankContainer.getMappedPort(2525)}"
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
                    .status(200)

        then:
        Imposter imp = cib.getImposter()
//        imp.getStubs().size() == 1
//        imp.getProtocol() == Protocol.HTTP
//        imp.getPort() == 4321
//        imp.getStub(0).getPredicates().size() == 1
//        imp.getStub(0).getPredicate(0).getEqulasParams().getMethod() == HttpMethod.POST
//        imp.getStub(0).getPredicate(0).getEqulasParams().getPath() == "/test"
//        imp.getStub(0).getPredicate(0).getEqulasParams().getHeaders() == ["Some-Header":"Header-Data"]
//        imp.getStub(0).getPredicate(0).getEqulasParams().getQueries() == ["q":"some query"]

        and:
//        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), impostersUrl)
//        isPosted == true
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
        imp.getStub(0).getPredicate(0).getEqulasParams().getQueries() == [
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
        imp.getStub(0).getPredicate(0).getEqulasParams().getHeaders() == [
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
                    .header("Response-Header1", "Header1")
                    .header("Response-header2", "Header2")
        then:
        Imposter imp = cib.getImposter()
        imp.getStub(0).getResponses().get(0).getResponse().getStatus() == 200
        imp.getStub(0).getResponses().get(0).getResponse().getBody() == "Hello World!"
        imp.getStub(0).getResponses().get(0).getResponse().getHeaders() == [
                "Response-Header1":"Header1",
                "Response-header2":"Header2"
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
        imp.getStub(0).getResponses().get(0).getResponse().getStatus() == 200
        imp.getStub(0).getResponses().get(0).getResponse().getBody() == mapper.writer().writeValueAsString(rootNode)

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
        imp.getStub(0).getResponses().get(0).getResponse().getStatus() == 200
        imp.getStub(0).getResponses().get(1).getResponse().getStatus() == 201

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), impostersUrl)
        isPosted == true
        println cib.getImposterAsJsonString()
    }
}
