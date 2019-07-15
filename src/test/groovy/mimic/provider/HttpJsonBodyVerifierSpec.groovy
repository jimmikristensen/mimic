package mimic.provider

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import mimic.mountebank.imposter.ResponseFields
import mimic.mountebank.provider.ProviderResponse
import mimic.mountebank.provider.verifier.HttpJsonBodyVerifier
import spock.lang.Specification

class HttpJsonBodyVerifierSpec extends Specification {

    def "exact match between contract and provider is verified"() {
        given:
        def body = '{"key": "value"}'
        def contractResponseFields = new ResponseFields(body: body)
        def providerResponseFields = new ProviderResponse(201, null, [:], body)

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == true
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
        def providerResponseFields = new ProviderResponse(201, null, [:], providerJson.writer().writeValueAsString(pRootNode))

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == true
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
        def providerResponseFields = new ProviderResponse(201, null, [:], providerJson.writer().writeValueAsString(pRootNode))

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == true
    }

    def "comparison between contract and provider where only contract has a null body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: null)
        def providerResponseFields = new ProviderResponse(201, null, [:], '{"key": "value"}')

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == false
    }

    def "comparison between contract and provider where only provider has a null body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: '{"key": "value"}')
        def providerResponseFields = new ProviderResponse(201, null, [:], null)

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == false
    }

    def "comparison between contract and provider where both contract and provider has a null body verifies to false"() {
        given:
        def contractResponseFields = new ResponseFields(body: null)
        def providerResponseFields = new ProviderResponse(201, null, [:], null)

        when:
        def isVerified = new HttpJsonBodyVerifier().verify(contractResponseFields, providerResponseFields)

        then:
        isVerified == true
    }
}
