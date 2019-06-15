package mimic


import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.net.Protocol
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.net.http.MountebankClient
import mimic.mountebank.MountebankContainerBuilder
import mimic.mountebank.imposter.Imposter
import spock.lang.Shared
import spock.lang.Specification

class ConsumerImposterSpec extends Specification {

    @Shared
    def mountebankContainer = new MountebankContainerBuilder().managementPort(2525).build()

    def setup() {

    }

    def "posting simple imposter to mountebank is successful"() {
        given:
        def cib = new ConsumerImposterBuilder(4321, Protocol.HTTP)

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
        imp.getStubs().size() == 1
        imp.getProtocol() == Protocol.HTTP
        imp.getPort() == 4321
        imp.getStub(0).getPredicates().size() == 1
        imp.getStub(0).getPredicate(0).getEqulasParams().getMethod() == HttpMethod.POST
        imp.getStub(0).getPredicate(0).getEqulasParams().getPath() == "/test"
        imp.getStub(0).getPredicate(0).getEqulasParams().getHeaders() == ["Some-Header":"Header-Data"]
        imp.getStub(0).getPredicate(0).getEqulasParams().getQueries() == ["q":"some query"]

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), "http://localhost:${mountebankContainer.getMappedPort(2525)}/imposters")
        isPosted == true
        println cib.getImposterAsJsonString()
    }

    def "imposter stub with multiple queries succeeds"() {
        given:
        def cib = new ConsumerImposterBuilder(4321)

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
        imp.getStub(0).getPredicate(0).getEqulasParams().getQueries() == ["q":"some query", "p2":"some other query", "p3":"some third query"]

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), "http://localhost:${mountebankContainer.getMappedPort(2525)}/imposters")
        isPosted == true
        println cib.getImposterAsJsonString()
    }

    def "imposter stub with multiple headers succeeds"() {
        given:
        def cib = new ConsumerImposterBuilder(4321)

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
        imp.getStub(0).getPredicate(0).getEqulasParams().getHeaders() == ["Some-Header":"Header-Data", "Some-Other-Header":"Header-Data2", "Some-Third-Header":"Header-Data3"]

        and:
        boolean isPosted = new MountebankClient().postImposter(cib.getImposterAsJsonString(), "http://localhost:${mountebankContainer.getMappedPort(2525)}/imposters")
        isPosted == true
        println cib.getImposterAsJsonString()
    }
}
