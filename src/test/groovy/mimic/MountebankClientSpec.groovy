package mimic

import mimic.mountebank.MountebankContainerBuilder
import mimic.mountebank.net.http.MountebankClient
import mimic.mountebank.net.http.exception.InvalidImposterException
import spock.lang.Shared
import spock.lang.Specification


class MountebankClientSpec extends Specification {

    @Shared
    def mbContainer

    def setupSpec() {
        mbContainer = new MountebankContainerBuilder().managementPort(2525).build()
    }

    def "imposter is successfully posted to mountebank server"() {
        given:
        def impostersUrl = "http://localhost:${mbContainer.getMappedPort(2525)}/imposters"
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'

        when:
        def isPosted = new MountebankClient().postImposter(imposterStr, impostersUrl)

        then:
        isPosted == true
        new MountebankClient().getImposters(impostersUrl).size() == 1
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
        InvalidImposterException ex = thrown()
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