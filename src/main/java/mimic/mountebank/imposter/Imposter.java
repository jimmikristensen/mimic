package mimic.mountebank.imposter;

import java.util.ArrayList;
import java.util.List;

public class Imposter {

    private int port;
    private String protocol;
    private List<Stub> stubs = new ArrayList<>();

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<Stub> getStubs() {
        return stubs;
    }

    public void addStubs(Stub stub) {
        stubs.add(stub);
    }
}
