package mimic.mountebank;

import mimic.mountebank.fragment.PredicateBuilder;
import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.imposter.Stub;

public class ConsumerImposterBuilder {

    private Imposter imposter;

    public ConsumerImposterBuilder(int port, String protocol) {
        imposter = new Imposter();
        imposter.setPort(port);
        imposter.setProtocol(protocol);
    }

    public PredicateBuilder givenRequest() {
        Stub stub = new Stub();
        imposter.addStubs(stub);
//        ImposterPredicateEquals ipe = new ImposterPredicateEquals();
//        stub.addPredicate(ipe);
//        imposter.stubs.add(stub);

        return new PredicateBuilder(stub);
    }

    public Imposter getImposter() {
        return imposter;
    }
}
