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
        new MountebankClient().deleteAllImposters("${mountebankUrl}/imposters")
    }

    def "after posting an imposter it should be possible to retrieve it from mountebank"() {
        given:
        def impostersUrl = "http://localhost:${mbContainer.getMappedPort(2525)}/imposters"
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'

        when:
        def mbClient = new MountebankClient(mbContainer)
        def isPosted = mbClient.postImposter(imposterStr, impostersUrl)

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

    def "trying to fetch imposter non-existing imposter results in MountebankCommunicationException"() {
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
        def impostersUrl = "http://localhost:${mbContainer.getMappedPort(2525)}/imposters"
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'

        when:
        def mbClient = new MountebankClient(mbContainer)
        mbClient.postImposter(imposterStr, impostersUrl)
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
        def impostersUrl = "http://localhost:${mbContainer.getMappedPort(2525)}/imposters"
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'

        when:
        def mbClient = new MountebankClient(mbContainer)
        mbClient.postImposter(imposterStr, impostersUrl)
        mbClient.getImposter("http://localhost:${mbContainer.getMappedPort(2525)}/imposters/four-three-two-one")

        then:
        InvalidImposterURLException ex = thrown()
        ex.message == "Unable to parse imposter port number"

    }

    def "posting invalid imposter results in exception"() {
        given:
        def impostersUrl = "http://localhost:${mbContainer.getMappedPort(2525)}/imposters"
        def malformedImposterStr = '{"port":4321,"protokoll":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'

        when:
        new MountebankClient().postImposter(malformedImposterStr, impostersUrl)

        then:
        new MountebankClient().getImposters(impostersUrl).size() == 0

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

    def "posting one imposter and getting the list of imposter URLs results in an array of one URLs being returned"() {
        given:
        def impostersUrl = "http://localhost:${mbContainer.getMappedPort(2525)}/imposters"
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'

        when:
        new MountebankClient(mbContainer).postImposter(imposterStr, impostersUrl)
        def imposterUrlList = new MountebankClient(mbContainer).getImposter(4321)

        then:
        println imposterUrlList.getPort()
        println imposterUrlList.getProtocol()
        println imposterUrlList.getStub(0).getResponse(0).getFields().getStatus()
    }

    def "deleting imposters when none exists results in an exception"() {

    }

    def "deleting all existing imposters is successful"() {

    }



}