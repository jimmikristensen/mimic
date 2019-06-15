package mimic.mountebank.imposter;

import mimic.mountebank.net.Protocol;

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

    public Protocol getProtocol() {
        return Protocol.valueOf(protocol.toUpperCase());
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol.getProtocol();
    }

    public List<Stub> getStubs() {
        return stubs;
    }

    public Stub getStub(int index) {
        return stubs.get(index);
    }

    public void addStubs(Stub stub) {
        stubs.add(stub);
    }
}
