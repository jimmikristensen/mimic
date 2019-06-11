package mimic

import mimic.mountebank.ConsumerImposterBuilder
import spock.lang.Specification

class ConsumerImposterSpec extends Specification {

    def "creating predicates"() {
        given:
        def cip = new ConsumerImposterBuilder(4321, "HTTP")

        when:
        def predicate = cip
                .requestEquals()
                    .method("POST")
                    .path("/test")
                    .query("q", "some query")
                    .header("Some-Header", "Header-Data")
                .respondsWith()
                    .status(200)
                .toImposterString();

        then:
        predicate.method == "POST"
    }
}
