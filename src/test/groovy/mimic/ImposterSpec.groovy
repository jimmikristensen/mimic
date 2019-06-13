package mimic

import mimic.mountebank.imposter.Imposter
import mimic.mountebank.imposter.EqualsParams
import mimic.mountebank.imposter.Stub
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification


class ImposterSpec extends Specification {

    def "converting single stub with one imposter and one response to json succeeds"() {
        given:
        def imposter = new Imposter(port: 1234, protocol: "HTTP", stubs: [new Stub(predicates: [new EqualsParams(name: "This is a equals")])])

        expect:
        ObjectMapper mapper = new ObjectMapper()
        mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, imposter)
    }

}