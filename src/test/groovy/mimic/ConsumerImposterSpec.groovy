package mimic


import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.MountebankClient
import mimic.mountebank.MountebankContainerBuilder
import mimic.mountebank.imposter.Imposter
import spock.lang.Shared
import spock.lang.Specification

class ConsumerImposterSpec extends Specification {

    @Shared
    def mountebankContainer = new MountebankContainerBuilder().managementPort(2525).build()


    def "imposter containers a stub with one predicate after populating"() {
        given:
        def cib = new ConsumerImposterBuilder(4321, "http")

        when:
        def predicate = cib
                .givenRequest()
                .equals()
                .method("POST")
                .path("/test")
                .query("q", "some query")
                .header("Some-Header", "Header-Data")

        then:
        Imposter imp = cib.getImposter()
        imp.getStubs().size() == 1
        imp.getProtocol() == "HTTP"
        imp.getPort() == 4321
        imp.getStubs().get(0).getPredicates().size() == 1
        imp.getStubs().get(0).getPredicates().get(0).getEqulasParams().getMethod() == "POST"
        imp.getStubs().get(0).getPredicates().get(0).getEqulasParams().getPath() == "/test"
        imp.getStubs().get(0).getPredicates().get(0).getEqulasParams().getHeaders() == ["Some-Header":"Header-Data"]
        imp.getStubs().get(0).getPredicates().get(0).getEqulasParams().getQueries() == ["q":"some query"]

        and:
        println "http://localhost:${mountebankContainer.getMappedPort(2525)}"
        new MountebankClient().writeImposter(cib.getImposterAsJsonString(), "http://localhost:${mountebankContainer.getMappedPort(2525)}/imposters")
        println cib.getImposterAsJsonString()
        Thread.sleep(60000)
    }

    def "imposter contains a stub with two equals predicates"() {
        given:
        def cib = new ConsumerImposterBuilder(4321, "HTTP")

        when:
        def predicate = cib
                .givenRequest()
                .equals()
                .method("POST")
                .path("/test")
                .query("q", "some query")
                .header("Some-Header", "Header-Data")
                .and()
                .equals()
                .header("Some-Other-Header", "Header-Data2")

        then:
        println cib.getImposterAsJsonString()
    }
}
