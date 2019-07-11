package mimic.mountebank.imposter;

import mimic.mountebank.net.http.HttpMethod;

import java.util.HashMap;

public class Equals implements HttpPredicate {

    private String method;
    private String path;
    private String body;
    private HashMap<String, String> query = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.valueOf(method);
    }

    @Override
    public String getMethodAsString() {
        return method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void setMethod(HttpMethod method) {
        this.method = method.name();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public HashMap<String, String> getQueries() {
        return query;
    }

    @Override
    public void addQuery(String key, String value) {
        this.query.put(key, value);
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }
}
