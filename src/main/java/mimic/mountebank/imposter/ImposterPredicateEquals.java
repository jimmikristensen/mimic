package mimic.mountebank.imposter;

import java.util.HashMap;

public class ImposterPredicateEquals {

    public ImposterPredicateEqualsParams equals;

    private ImposterPredicateEqualsParams ipep;

    public ImposterPredicateEquals() {
        ImposterPredicateEqualsParams ipep = new ImposterPredicateEqualsParams();
        this.ipep = ipep;
        equals = this.ipep;
    }

    public void setMethod(String method) {
        ipep.method = method;
    }

    public void setPath(String path) {
        ipep.path = path;
    }

    public void setQueries(String key, String value) {
        ipep.queries.put(key, value);
    }

    public void setHeaders(String key, String value) {
        ipep.headers.put(key, value);
    }

    private class ImposterPredicateEqualsParams {
        public String method;
        public String path;
        public HashMap<String, String> queries = new HashMap<>();
        public HashMap<String, String> headers = new HashMap<>();
    }
}
