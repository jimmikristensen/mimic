package mimic.provider

import mimic.mountebank.provider.verifier.results.diff.Diff
import mimic.mountebank.provider.verifier.results.diff.DiffOperation
import mimic.mountebank.provider.verifier.results.diff.DiffSet
import spock.lang.Specification

class DiffSetSpec extends Specification {

    def "adding a single diff to the set will store the diff"() {
        given:
        def diffSet = new DiffSet()
        diffSet.add(DiffSet.Type.BODY, new Diff(
                Diff.Type.JSON,
                DiffOperation.REMOVE,
                "/key",
                null,
                null
        ))

        expect:
        def diffList = diffSet.get(DiffSet.Type.BODY)
        diffList.size() == 1

        and:
        diffList.get(0).getPath() == "/key"
        diffList.get(0).getOperation() == DiffOperation.REMOVE
        diffList.get(0).getValue() == null
        diffList.get(0).getFrom() == null
    }

    def "adding a two diff to the set will store the diff"() {
        given:
        def diffSet = new DiffSet()
        diffSet.add(DiffSet.Type.BODY, new Diff(
                Diff.Type.JSON,
                DiffOperation.REMOVE,
                "/key",
                null,
                null
        ))
        diffSet.add(DiffSet.Type.BODY, new Diff(
                Diff.Type.JSON,
                DiffOperation.REPLACE,
                null,
                "some text",
                "some text 2"
        ))

        expect:
        def diffList = diffSet.get(DiffSet.Type.BODY)
        diffList.size() == 2

        and:
        diffList.get(0).getPath() == "/key"
        diffList.get(0).getOperation() == DiffOperation.REMOVE
        diffList.get(0).getValue() == null
        diffList.get(0).getFrom() == null

        and:
        diffList.get(1).getPath() == null
        diffList.get(1).getOperation() == DiffOperation.REPLACE
        diffList.get(1).getValue() == "some text"
        diffList.get(1).getFrom() == "some text 2"
    }

    def "retrieving the diffset types is successful when exists"() {
        given:
        def diffSet = new DiffSet()
        diffSet.add(DiffSet.Type.BODY, new Diff(
                Diff.Type.JSON,
                DiffOperation.REMOVE,
                "/key",
                null,
                null
        ))
        diffSet.add(DiffSet.Type.HEADER, new Diff(
                Diff.Type.TEXT,
                DiffOperation.EQUAL,
                null,
                null,
                null
        ))

        expect:
        Set typeSet = diffSet.getTypes()
        Set resultSet = [DiffSet.Type.BODY, DiffSet.Type.HEADER]
        typeSet == resultSet
    }
}
