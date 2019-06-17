package mimic.mountebank.fragment;

import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.imposter.Stub;
import mimic.mountebank.net.Protocol;

public class ImposterBuilder {

    private Imposter imposter;

    public ImposterBuilder(Imposter imposter) {
        this.imposter = imposter;
    }

    public PredicateBuilder givenRequest(int port, Protocol protocol) {
        imposter.setPort(port);
        imposter.setProtocol(protocol);

        Stub stub = new Stub();
        imposter.addStubs(stub);

        return new PredicateBuilder(stub);
    }

    public PredicateBuilder givenRequest(int port) {
        return givenRequest(port, Protocol.HTTP);
    }
}
