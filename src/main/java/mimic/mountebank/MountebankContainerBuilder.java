
package mimic.mountebank;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.ArrayList;
import java.util.List;

class MountebankContainerBuilder {

    private String dockerImage = "jkris/mountebank";
    private List<Integer> exposedPorts = new ArrayList<>();
    private int managementPort = 2525;

    public MountebankContainerBuilder managementPort(int managementPort) {
        this.managementPort = managementPort;
        exposedPorts.add(managementPort);
        return this;
    }

    public MountebankContainerBuilder stubPort(int stubPort) {
        exposedPorts.add(stubPort);
        return this;
    }

    public GenericContainer build() {
        GenericContainer container = new GenericContainer(dockerImage)
                .withExposedPorts(exposedPorts.stream().toArray(Integer[]::new))
                .waitingFor(
                        Wait.forHttp("/").forPort(managementPort)
                );

        container.start();
        return container;
    }

}
