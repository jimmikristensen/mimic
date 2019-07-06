package mimic.mountebank.consumer.dsl;

import mimic.mountebank.imposter.Equals;
import mimic.mountebank.imposter.Predicate;
import mimic.mountebank.imposter.Stub;

public class PredicateBuilder {

    private Stub stub;

    public PredicateBuilder(Stub stub) {
        this.stub = stub;
    }

    public PredicateEqualsBuilder equals() {
        Predicate predicate = new Predicate();
        Equals equals = new Equals();
        predicate.setEquals(equals);
        stub.addPredicate(predicate);
        return new PredicateEqualsBuilder(equals, this, new ResponseBuilder(stub));
    }
}
