package mimic.provider

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.verifier.HttpJsonBodyVerifier
import mimic.mountebank.provider.verifier.results.diff.DiffOperation
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult
import mimic.mountebank.provider.verifier.results.ReportStatus
import spock.lang.Specification

class HttpJsonBodyVerifierSpec extends Specification {

    def "exact match between contract and provider is verified"() {
        given:
        def body = '{"key": "value"}'
        def contractResponseFields = new ResponseFields(body: body)
        def providerResponseFields = new ProviderHTTPResult(responseBody: body)

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.getDiff().size() == 0
    }

    def "same json keys and values, but in different order, between contract and provider is verified"() {
        given:
        def contractJson = new ObjectMapper()
        JsonNode cRootNode = contractJson.createObjectNode()
        JsonNode cNode = contractJson.createObjectNode()
        ((ObjectNode) cNode).put("key1", "value1")
        ((ObjectNode) cNode).put("key2", "value2")
        ((ObjectNode) cRootNode).set("test", cNode)

        def providerJson = new ObjectMapper()
        JsonNode pRootNode = providerJson.createObjectNode()
        JsonNode pNode = providerJson.createObjectNode()
        ((ObjectNode) pNode).put("key2", "value2")
        ((ObjectNode) pNode).put("key1", "value1")
        ((ObjectNode) pRootNode).set("test", pNode)

        and:
        def contractResponseFields = new ResponseFields(body: contractJson.writer().writeValueAsString(cRootNode))
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerJson.writer().writeValueAsString(pRootNode))

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.getDiff().size() == 0
    }

    def "comparison between contract and provider is verified when json contains arrays"() {
        given:
        def contractJson = new ObjectMapper()
        JsonNode cRootNode = contractJson.createObjectNode()
        JsonNode cNode = contractJson.createObjectNode()
        cNode.put("key1", "value1")
        cNode.put("key2", "value2")

        JsonNode cArrayItem = contractJson.createObjectNode()
        cArrayItem.put("id0", "item0")
        cArrayItem.put("id1", "item1")

        ArrayNode cArrayNode = contractJson.createArrayNode()
        cArrayNode.add(cArrayItem)
        cNode.set("objectArray",cArrayNode)
        cRootNode.set("test", cNode)

        def providerJson = new ObjectMapper()
        JsonNode pRootNode = providerJson.createObjectNode()
        JsonNode pNode = providerJson.createObjectNode()
        pNode.put("key2", "value2")
        pNode.put("key1", "value1")

        JsonNode pArrayItem = providerJson.createObjectNode()
        pArrayItem.put("id0", "item0")
        pArrayItem.put("id1", "item1")

        ArrayNode pArrayNode = providerJson.createArrayNode()
        pArrayNode.add(pArrayItem)
        pNode.set("objectArray", pArrayNode)
        pRootNode.set("test", pNode)

        and:
        def contractResponseFields = new ResponseFields(body: contractJson.writer().writeValueAsString(cRootNode))
        def providerResponseFields = new ProviderHTTPResult(responseBody: providerJson.writer().writeValueAsString(pRootNode))

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.getDiff().size() == 0
    }

    def "comparison between contract and provider where contract contains fields not present verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: '{"key": "other value","key3": "value2"}')
        def providerResponseFields = new ProviderHTTPResult(responseBody: '{"key": "value","key2": "value2","test":"testval"}')

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() == 3

        and:
        diff.get(0).getOperation() == DiffOperation.REMOVE
        diff.get(0).getPath() == "/test"
        diff.get(0).getValue() == null
        diff.get(1).getOperation() == DiffOperation.MOVE
        diff.get(1).getPath() == "/key3"
        diff.get(1).getValue() == null
        diff.get(1).getFrom() == "/key2"
        diff.get(2).getOperation() == DiffOperation.REPLACE
        diff.get(2).getPath() == "/key"
        diff.get(2).getValue() == 'other value'
    }

    def "with complex json,comparison between contract and provider where contract contains fields not present verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: complexJson1())
        def providerResponseFields = new ProviderHTTPResult(responseBody: complexJson2())

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        verificationResult.getDiff().size() == 12
    }

    def "comparison between contract and provider where only contract has a null body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: null)
        def providerResponseFields = new ProviderHTTPResult(responseBody: '{"key": "value"}')

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() == 1

        and:
        diff.get(0).getOperation() == DiffOperation.REMOVE
        diff.get(0).getPath() == "/key"
    }

    def "comparison between contract and provider where only provider has a null body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: '{"key": "value"}')
        def providerResponseFields = new ProviderHTTPResult(responseBody: null)

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() == 1

        and:
        diff.get(0).getOperation() == DiffOperation.ADD
        diff.get(0).getPath() == "/key"
        diff.get(0).getValue() == "value"
    }

    def "comparison between contract and provider where both contract and provider has a null body is verified OK"() {
        given:
        def contractResponseFields = new ResponseFields(body: null)
        def providerResponseFields = new ProviderHTTPResult(responseBody: null)

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
    }

    def "comparison between contract and provider where only provider has an empty body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: '{"key": "value"}')
        def providerResponseFields = new ProviderHTTPResult(responseBody: "")

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() == 1

        and:
        diff.get(0).getOperation() == DiffOperation.ADD
        diff.get(0).getPath() == "/key"
        diff.get(0).getValue() == "value"
    }

    def "comparison between contract and provider where only contract has an empty body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: "")
        def providerResponseFields = new ProviderHTTPResult(responseBody: '{"key": "value"}')

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.FAILED
        def diff = verificationResult.getDiff()
        diff.size() == 1

        and:
        diff.get(0).getOperation() == DiffOperation.REMOVE
        diff.get(0).getPath() == "/key"
    }

    def "comparison between contract and provider where both contract and provider has an empty body is verified OK"() {
        given:
        def contractResponseFields = new ResponseFields(body: "")
        def providerResponseFields = new ProviderHTTPResult(responseBody: "")

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        verificationResult.getReportStatus() == ReportStatus.OK
    }

    private String complexJson1() {
        return '{\n' +
                '  "name": {\n' +
                '    "first": "John",\n' +
                '    "last": "Doe"\n' +
                '  },\n' +
                '  "address": null,\n' +
                '  "birthday": "1980-01-01",\n' +
                '  "company": "Acme",\n' +
                '  "occupation": "Software engineer",\n' +
                '  "phones": [\n' +
                '    {\n' +
                '      "number": "000000000",\n' +
                '      "type": "home"\n' +
                '    },\n' +
                '    {\n' +
                '      "number": "999999999",\n' +
                '      "type": "mobile"\n' +
                '    }\n' +
                '  ]\n' +
                '}';
    }

    private String complexJson2() {
        return '{\n' +
                '  "name": {\n' +
                '    "first": "Jane",\n' +
                '    "last": "Doe",\n' +
                '    "nickname": "Jenny"\n' +
                '  },\n' +
                '  "birthday": "1990-01-01",\n' +
                '  "occupation": null,\n' +
                '  "phones": [\n' +
                '    {\n' +
                '      "number": "111111111",\n' +
                '      "type": "mobile",\n' +
                '      "type2": "block"\n' +
                '    }\n' +
                '  ],\n' +
                '  "favorite": true,\n' +
                '  "groups": [\n' +
                '    "close-friends",\n' +
                '    "gym"\n' +
                '  ]\n' +
                '}';
    }
}
