package mimic.mountebank.fragment;

import mimic.mountebank.imposter.EqualsParams;
import mimic.mountebank.net.http.HttpMethod;

public class PredicateEqualsBuilder {

    private EqualsParams equals;
    private PredicateBuilder predicateBuilder;
    private ResponseBuilder responseBuilder;

    public PredicateEqualsBuilder(EqualsParams equals, PredicateBuilder predicateBuilder, ResponseBuilder responseBuilder) {
        this.equals = equals;
        this.predicateBuilder = predicateBuilder;
        this.responseBuilder = responseBuilder;
    }

    public PredicateEqualsBuilder method(String method) {
        equals.setMethod(method);
        return this;
    }

    public PredicateEqualsBuilder method(HttpMethod method) {
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

    public ResponseBuilder respondsWith() {
        return responseBuilder;
    }

    public PredicateBuilder and() {
        return predicateBuilder;
    }
}
