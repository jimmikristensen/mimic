package mimic.mountebank.fragment;

import mimic.mountebank.imposter.Equals;
import mimic.mountebank.imposter.EqualsParams;
import mimic.mountebank.imposter.Predicate;
import mimic.mountebank.imposter.Stub;

public class PredicateBuilder {

    private Stub stub;

    public PredicateBuilder(Stub stub) {
        this.stub = stub;
    }

    public PredicateEqualsBuilder equals() {
        Predicate predicate = new Predicate();
        EqualsParams equals = new EqualsParams();
        predicate.setEquals(equals);
        stub.addPredicate(predicate);
        return new PredicateEqualsBuilder(equals, this, new ResponseBuilder(stub));
    }
}
