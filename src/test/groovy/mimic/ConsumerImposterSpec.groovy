package mimic


import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.imposter.Imposter
import spock.lang.Specification

class ConsumerImposterSpec extends Specification {


    def "imposter containers a stub with one predicate after populating"() {
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
        println cib.getImposterAsJsonString()
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

        then:
        println cib.getImposterAsJsonString()
    }
}
