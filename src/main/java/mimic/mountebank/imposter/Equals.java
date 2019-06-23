package mimic.mountebank.imposter;

import mimic.mountebank.net.http.HttpMethod;

import java.util.HashMap;

public class Equals {

    private String method;
    private String path;
    private String body;
    private HashMap<String, String> query = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();

    public HttpMethod getMethod() {
        return HttpMethod.valueOf(method);
    }

    public String getMethodAsString() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method.name();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, String> getQueries() {
        return query;
    }

    public void addQuery(String key, String value) {
        this.query.put(key, value);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
