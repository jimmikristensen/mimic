package mimic.mountebank;

import mimic.mountebank.fragment.PredicateEquals;
import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.imposter.ImposterPredicateEquals;
import mimic.mountebank.imposter.Stub;

public class ConsumerImposterBuilder {

    private Imposter imposter;

    public ConsumerImposterBuilder(int port, String protocol) {
        imposter = new Imposter();
        imposter.port = port;
        imposter.protocol = protocol;
    }

    public PredicateEquals requestEquals() {
        Stub stub = new Stub();
        ImposterPredicateEquals ipe = new ImposterPredicateEquals();
        stub.predicates.add(ipe);
        imposter.stubs.add(stub);

        return new PredicateEquals(this, ipe);
    }

    public Imposter getImposter() {
        return imposter;
    }
}
