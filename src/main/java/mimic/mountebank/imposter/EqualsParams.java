package mimic.mountebank.imposter;

import java.util.HashMap;

public class EqualsParams {

    private String method;
    private String path;
    private HashMap<String, String> queries = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, String> getQueries() {
        return queries;
    }

    public void addQuery(String key, String value) {
        this.queries.put(key, value);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }
}
