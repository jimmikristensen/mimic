package mimic

import mimic.mountebank.MountebankContainerBuilder
import mimic.mountebank.net.http.MountebankClient
import mimic.mountebank.net.http.exception.InvalidImposterException
import spock.lang.Shared
import spock.lang.Specification


class MountebankClientSpec extends Specification {

    @Shared
    def mountebankContainer

    def setupSpec() {
        mountebankContainer = new MountebankContainerBuilder().managementPort(2525).build()
    }

    def "imposter is successfully posted to mountebank server"() {
        given:
        def impostersUrl = "http://localhost:${mountebankContainer.getMappedPort(2525)}/imposters"
        def imposterStr = '{"port":4321,"protocol":"http","stubs":[{"responses":[{"is":{"statusCode":201}}]}]}'

        when:
        def isPosted = new MountebankClient().postImposter(imposterStr, impostersUrl)

        then:
        isPosted == true
        new MountebankClient().getImposters(impostersUrl).size() == 1
    }

    def "posting invalid imposter results in exception"() {
        given:
        def impostersUrl = "http://localhost:${mountebankContainer.getMappedPort(2525)}/imposters"
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
}