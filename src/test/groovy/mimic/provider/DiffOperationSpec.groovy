package mimic.provider

import com.fasterxml.jackson.databind.JsonNode
import mimic.mountebank.net.databind.JacksonObjectMapper
import mimic.mountebank.provider.verifier.results.diff.DiffOperation
import spock.lang.Specification

class DiffOperationSpec extends Specification {

    def "valid DiffOperation names returns the enum correct type"() {
        when:
        def opEqual = DiffOperation.getEnum("EQUAL")

        then:
        opEqual == DiffOperation.EQUAL

        when:
        def opRemove = DiffOperation.getEnum("REMOVE")

        then:
        opRemove == DiffOperation.REMOVE

        when:
        def opDelete = DiffOperation.getEnum("DELETE")

        then:
        opDelete == DiffOperation.REMOVE

        when:
        def opAdd = DiffOperation.getEnum("ADD")

        then:
        opAdd == DiffOperation.ADD

        when:
        def opInsert = DiffOperation.getEnum("INSERT")

        then:
        opInsert == DiffOperation.ADD

        when:
        def opReplace = DiffOperation.getEnum("REPLACE")

        then:
        opReplace == DiffOperation.REPLACE

        when:
        JsonNode node = JacksonObjectMapper.getMapper().readTree('{"op":"remove","path":"/key"}');
        String opName = node.get("op").asText().toUpperCase().trim();
        DiffOperation opRem = DiffOperation.getEnum(opName)

        then:
        opRem == DiffOperation.REMOVE
    }

    def "asking for a DiffOperation that does not exist"() {
        when:
        def opUnknown = DiffOperation.getEnum("DOESNOTEXIST")

        then:
        NoSuchElementException ex = thrown()
    }
}
