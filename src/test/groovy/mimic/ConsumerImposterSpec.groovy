package mimic

import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.imposter.Imposter
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class ConsumerImposterSpec extends Specification {

    def "creating predicates"() {
        given:
        def cip = new ConsumerImposterBuilder(4321, "HTTP")

        when:
        def imposter = cip
                .requestEquals()
                    .method("POST")
                    .path("/test")
                    .query("q", "some query")
                    .header("Some-Header", "Header-Data")
                .respondsWith()
                    .status(200)
                .toImposterString()

        then:
        println imposter
    }

    def "imposter containers a stub with one predicate after populating"() {
        given:
        def cib = new ConsumerImposterBuilder(4321, "HTTP")

        when:
        def predicate = cib
                .requestEquals()
                .method("POST")
                .path("/test")
                .query("q", "some query")
                .header("Some-Header", "Header-Data")

        then:
        Imposter imp = cib.getImposter()
        imp.stubs.size() == 1
        imp.protocol == "HTTP"
        imp.port == 4321
        imp.stubs.get(0).predicates.size() == 1
        imp.stubs.get(0).predicates.get(0).equals.method == "POST"
        imp.stubs.get(0).predicates.get(0).equals.path == "/test"
        imp.stubs.get(0).predicates.get(0).equals.headers == ["Some-Header":"Header-Data"]
        imp.stubs.get(0).predicates.get(0).equals.queries == ["q":"some query"]

        and:
        ObjectMapper mapper = new ObjectMapper()
        mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, imp)
    }
}
