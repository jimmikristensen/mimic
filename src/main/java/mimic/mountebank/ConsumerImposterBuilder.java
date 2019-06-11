package mimic.mountebank;

import mimic.mountebank.fragment.PredicateEquals;
import mimic.mountebank.fragment.Stub;

import java.util.List;

public class ConsumerImposterBuilder {

    private int port;
    private String protocol;
    private List<Stub> stubs;

    public ConsumerImposterBuilder(int port, String protocol) {
        this.port = port;
        this.protocol = protocol;
    }

    public PredicateEquals requestEquals() {
        Stub stub = new Stub(this);
        stubs.add(stub);
        return stub.createStub();
    }
}
