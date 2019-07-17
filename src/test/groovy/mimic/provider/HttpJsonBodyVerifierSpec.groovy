package mimic.provider

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.verifier.HttpJsonBodyVerifier
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult
import mimic.mountebank.provider.verifier.results.ReportStatus
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
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
        verificationResult.bodyDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
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
        println verificationResult.bodyDiff()
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.bodyDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
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
        println verificationResult.bodyDiff()
        verificationResult.getReportStatus() == ReportStatus.OK
        verificationResult.bodyDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
    }

    def "comparison between contract and provider where contract contains fields not present verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: '{"key2": "other value"}')
        def providerResponseFields = new ProviderHTTPResult(responseBody: '{"key": "value"}')

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        println verificationResult.bodyDiff()
        verificationResult.getReportStatus() == ReportStatus.FAILED
        verificationResult.bodyDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
    }

    def "with complex json,comparison between contract and provider where contract contains fields not present verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: complexJson1())
        def providerResponseFields = new ProviderHTTPResult(responseBody: complexJson2())

        when:
        def verificationResult = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        println verificationResult.bodyDiff()
        verificationResult.getReportStatus() == ReportStatus.FAILED
        verificationResult.bodyDiff().get(0).operation == DiffMatchPatch.Operation.EQUAL
    }

    def "comparison between contract and provider where only contract has a null body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: null)
        def providerResponseFields = new ProviderHTTPResult(responseBody: '{"key": "value"}')

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == false
    }

    def "comparison between contract and provider where only provider has a null body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: '{"key": "value"}')
        def providerResponseFields = new ProviderHTTPResult(responseBody: null)

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == false
    }

    def "comparison between contract and provider where both contract and provider has a null body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: null)
        def providerResponseFields = new ProviderHTTPResult(responseBody: null)

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == true
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
                '      "type": "mobile"\n' +
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
