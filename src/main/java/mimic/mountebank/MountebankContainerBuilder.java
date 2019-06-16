
package mimic.mountebank;

import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;

import java.util.ArrayList;
import java.util.List;

class MountebankContainerBuilder {

    private String dockerImage = "jkris/mountebank";
    private List<Integer> exposedPorts = new ArrayList<>();

    public MountebankContainerBuilder managementPort(int managementPort) {
        exposedPorts.add(managementPort);
        return this;
    }

    public MountebankContainerBuilder exposePort(int exposePort) {
        exposedPorts.add(exposePort);
        return this;
    }

    public GenericContainer build() {
        GenericContainer container = new GenericContainer(dockerImage)
                .withExposedPorts(exposedPorts.stream().toArray(Integer[]::new));

        container.start();
        return container;
    }

}
