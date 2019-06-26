package mimic.mountebank;

import org.testcontainers.containers.GenericContainer;

public class MountebankContainer extends GenericContainer {

    public MountebankContainer() {
        super("jkris/mountebank");
    }

}
