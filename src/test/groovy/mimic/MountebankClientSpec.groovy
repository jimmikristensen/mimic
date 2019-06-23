package mimic

import mimic.mountebank.MountebankContainerBuilder
import mimic.mountebank.net.http.MountebankClient
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
        new MountebankClient().postImposter(imposterStr, impostersUrl)

        then:
        new MountebankClient().getImposters(impostersUrl).size() == 1
    }

}