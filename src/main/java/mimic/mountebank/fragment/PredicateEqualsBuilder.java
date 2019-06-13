package mimic.mountebank.fragment;

import mimic.mountebank.imposter.EqualsParams;

public class PredicateEqualsBuilder {

    private EqualsParams equals;
    private PredicateBuilder predicateBuilder;

    public PredicateEqualsBuilder(EqualsParams equals, PredicateBuilder predicateBuilder) {
        this.equals = equals;
        this.predicateBuilder = predicateBuilder;
    }

    public PredicateEqualsBuilder method(String method) {
        equals.setMethod(method);
        return this;
    }

    public PredicateEqualsBuilder path(String path) {
        equals.setPath(path);
        return this;
    }

    public PredicateEqualsBuilder query(String key, String value) {
        equals.addQuery(key, value);
        return this;
    }

    public PredicateEqualsBuilder header(String key, String value) {
        equals.addHeader(key, value);
        return this;
    }

    public void respondsWith() {

    }

    public PredicateBuilder and() {
        return predicateBuilder;
    }
}
