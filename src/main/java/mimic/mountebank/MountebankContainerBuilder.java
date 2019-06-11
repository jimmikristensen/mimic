
package mimic.mountebank;

import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;

class MountebankContainerBuilder {

    private int managementPort = 2525;
    private String dockerImage = "jkris/mountebank";

    public MountebankContainerBuilder() {}

    public MountebankContainerBuilder managementPort(int managementPort) {
        this.managementPort = managementPort;
        return this;
    }

    public GenericContainer build() {
        GenericContainer container = new GenericContainer(dockerImage)
                .withExposedPorts(managementPort);
        container.start();
        return container;
    }

}
