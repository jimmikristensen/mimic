package mimic

import mimic.mountebank.MountebankContainerBuilder
import spock.lang.Specification

class MountebankContainerBuilderSpec extends Specification {

    def "Mountebank container can be started and stopped"() {
        given:
        def mbc = new MountebankContainerBuilder().managementPort(2525).build()

        expect:
        mbc.isRunning() == true
        mbc.getDockerImageName() == "jkris/mountebank:latest"

        and:
        mbc.stop()
        mbc.isRunning() == false
    }
}
