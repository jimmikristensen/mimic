package mimic.mountebank.fragment;

import mimic.mountebank.ConsumerImposterBuilder;
import mimic.mountebank.imposter.ImposterPredicate;

public class PredicateEquals {

    private ConsumerImposterBuilder cib;
    private ImposterPredicate imposterPredicate;

    public PredicateEquals(ConsumerImposterBuilder cib, ImposterPredicate imposterPredicate) {
        this.cib = cib;
        this.imposterPredicate = imposterPredicate;
    }

    public PredicateEquals method(String method) {
        imposterPredicate.method = method;
        return this;
    }

    public PredicateEquals path(String path) {
        imposterPredicate.path = path;
        return this;
    }

    public PredicateEquals query(String key, String value) {
        imposterPredicate.queries.put(key, value);
        return this;
    }

    public PredicateEquals header(String key, String value) {
        imposterPredicate.headers.put(key, value);
        return this;
    }

    public Response respondsWith() {
        return new Response(cib);
    }

}
