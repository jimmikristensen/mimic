package mimic

import mimic.mountebank.MountebankContainerBuilder
import mimic.mountebank.net.Protocol
import mimic.mountebank.net.http.MountebankClient
import mimic.mountebank.net.http.exception.MountebankCommunicationException
import mimic.mountebank.net.http.exception.InvalidImposterURLException
import spock.lang.Shared
import spock.lang.Specification


class MountebankClientSpec extends Specification {

    @Shared
    def mbContainer

    @Shared
    def mountebankUrl

    def setupSpec() {
        mbContainer = new MountebankContainerBuilder().managementPort(2525).stubPort(4321).build()
        mountebankUrl = "http://localhost:${mbContainer.getMappedPort(2525)}"
    }

    def setup() {
        new MountebankClient(mbContainer).deleteAllImposters()
    }

    def "after posting an imposter it should be possible to retrieve it from mountebank"() {
        given:
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'
        def mbClient = new MountebankClient(mbContainer)

        when:
        def isPosted = mbClient.postImposter(imposterStr)

        then:
        isPosted == true
        def imposter = mbClient.getImposter(4321)
        imposter != null

        and:
        imposter.getPort() == 4321
        imposter.getProtocol() == Protocol.HTTP
        imposter.getStub(0) != null
        imposter.getStub(0).getResponse(0).getFields().getStatus() == 201
    }

    def "trying to fetch non-existing imposter results in MountebankCommunicationException"() {
        given:
        def mbClient = new MountebankClient(mbContainer)

        when:
        mbClient.getImposter(4321)

        then:
        MountebankCommunicationException ex = thrown()
        ex.message == "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"code\": \"no such resource\",\n" +
                "      \"message\": \"Try POSTing to /imposters first?\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"
    }

    def "retrieving an imposter from URL is successful"() {
        given:
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'
        def mbClient = new MountebankClient(mbContainer)

        when:
        mbClient.postImposter(imposterStr)
        def imposter = mbClient.getImposter("http://localhost:${mbContainer.getMappedPort(2525)}/imposters/4321")

        then:
        imposter != null
        imposter.getPort() == 4321
        imposter.getProtocol() == Protocol.HTTP
        imposter.getStub(0) != null
        imposter.getStub(0).getResponse(0).getFields().getStatus() == 201
    }

    def "supplying invalid imposter url result in a InvalidImposterURLException"() {
        given:
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'
        def mbClient = new MountebankClient(mbContainer)

        when:
        mbClient.postImposter(imposterStr)
        mbClient.getImposter("http://localhost:${mbContainer.getMappedPort(2525)}/imposters/four-three-two-one")

        then:
        InvalidImposterURLException ex = thrown()
        ex.message == "Unable to parse imposter port number"

    }

    def "posting invalid imposter results in MountebankCommunicationException"() {
        given:
        def malformedImposterStr = '{"port":4321,"protokoll":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'
        def mbClient = new MountebankClient(mbContainer)

        when:
        mbClient.postImposter(malformedImposterStr)

        then:
        mbClient.getImposters().size() == 0

        and:
        MountebankCommunicationException ex = thrown()
        ex.message == '{\n' +
                '  "errors": [\n' +
                '    {\n' +
                '      "code": "bad data",\n' +
                '      "message": "\'protocol\' is a required field"\n' +
                '    }\n' +
                '  ]\n' +
                '}'
    }

    def "posting one imposter and getting the list of imposters results in an array of one being returned"() {
        given:
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'
        def mbClient = new MountebankClient(mbContainer)

        when:
        mbClient.postImposter(imposterStr)
        def imposters = mbClient.getImposters()

        then:
        imposters.size() == 1
        imposters.get(0).getPort() == 4321
        imposters.get(0).getProtocol() == Protocol.HTTP
        imposters.get(0).getStub(0).getResponse(0).getFields().getStatus() == 201
    }

    def "posting two imposters and getting the list of imposters results in an array of two being returned"() {
        given:
        def imposterStr1 = '{"port":1234,"protocol":"https","stubs":[{"responses":[{"is":{"statusCode":404}}]}]}'
        def imposterStr2 = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'
        def mbClient = new MountebankClient(mbContainer)

        when:
        mbClient.postImposter(imposterStr1)
        mbClient.postImposter(imposterStr2)
        def imposters = mbClient.getImposters()

        then:
        imposters.size() == 2
        imposters.get(0).getPort() == 1234
        imposters.get(0).getProtocol() == Protocol.HTTPS
        imposters.get(0).getStub(0).getResponse(0).getFields().getStatus() == 404

        and:
        imposters.get(1).getPort() == 4321
        imposters.get(1).getProtocol() == Protocol.HTTP
        imposters.get(1).getStub(0).getResponse(0).getFields().getStatus() == 201
    }

    def "deleting single imposter when none exists is allowed"() {
        given:
        def mbClient = new MountebankClient(mbContainer)

        when:
        boolean wasDeleted = mbClient.deleteImposter(8347)

        then:
        wasDeleted == true
    }

    def "deleting one imposter out of two results in one remaining"() {
        given:
        def imposterStr1 = '{"port":1234,"protocol":"https","stubs":[{"responses":[{"is":{"statusCode":404}}]}]}'
        def imposterStr2 = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'
        def mbClient = new MountebankClient(mbContainer)

        when:
        mbClient.postImposter(imposterStr1)
        mbClient.postImposter(imposterStr2)

        then:
        mbClient.getImposters().size() == 2

        when:
        mbClient.deleteImposter(1234)

        then:
        mbClient.getImposters().size() == 1
    }

    def "deleting all existing imposters is successful"() {
        given:
        def imposterStr1 = '{"port":1234,"protocol":"https","stubs":[{"responses":[{"is":{"statusCode":404}}]}]}'
        def imposterStr2 = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'
        def mbClient = new MountebankClient(mbContainer)

        when:
        mbClient.postImposter(imposterStr1)
        mbClient.postImposter(imposterStr2)

        then:
        mbClient.getImposters().size() == 2

        when:
        mbClient.deleteAllImposters()

        then:
        mbClient.getImposters().size() == 0
    }



}