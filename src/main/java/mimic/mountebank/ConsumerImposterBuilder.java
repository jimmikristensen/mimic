package mimic.mountebank;

import mimic.mountebank.fragment.PredicateEquals;
import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.imposter.ImposterPredicate;
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
        ImposterPredicate imposterPredicate = new ImposterPredicate();
        stub.predicates.add(imposterPredicate);
        imposter.stubs.add(stub);

        return new PredicateEquals(this, imposterPredicate);
    }

    public Imposter getImposter() {
        return imposter;
    }
}
