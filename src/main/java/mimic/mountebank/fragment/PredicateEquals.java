package mimic.mountebank.fragment;

import java.util.HashMap;

public class PredicateEquals {

    private String method;
    private String path;
    private String query;
    private HashMap<String, String> queries = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();

    public PredicateEquals method(String method) {
        this.method = method;
        return this;
    }

    public PredicateEquals path(String path) {
        this.path = path;
        return this;
    }

    public PredicateEquals query(String key, String value) {
        queries.put(key, value);
        return this;
    }

    public PredicateEquals header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Response respondsWith() {
        return new Response();
    }

}
