package mimic.mountebank.fragment;

import mimic.mountebank.imposter.EqualsParams;

public class PredicateEqualsBuilder {

    private EqualsParams equals;

    public PredicateEqualsBuilder(EqualsParams equals) {
        this.equals = equals;
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
}
