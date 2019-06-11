package mimic.mountebank.fragment;

import mimic.mountebank.ConsumerImposterBuilder;

import java.util.List;

public class Stub {

    private ConsumerImposterBuilder cib;

    public Stub(ConsumerImposterBuilder cib) {
        this.cib = cib;
    }

    public PredicateEquals createStub() {
        return new PredicateEquals();
    }
}
