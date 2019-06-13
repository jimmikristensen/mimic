package mimic.mountebank.fragment;

import mimic.mountebank.imposter.Equals;
import mimic.mountebank.imposter.EqualsParams;
import mimic.mountebank.imposter.Stub;

public class PredicateBuilder {

    private Stub stub;

    public PredicateBuilder(Stub stub) {
        this.stub = stub;
    }

    public PredicateEqualsBuilder equals() {
        Equals equals = new Equals();
        EqualsParams equalsParams = new EqualsParams();
        equals.setEqulasParams(equalsParams);
        stub.addPredicate(equals);
        return new PredicateEqualsBuilder(equalsParams, this);
    }
}
