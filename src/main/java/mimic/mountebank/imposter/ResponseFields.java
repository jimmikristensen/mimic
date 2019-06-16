package mimic.mountebank.imposter;

import java.util.HashMap;

public class ResponseFields {

    private int statusCode;
    private String body;
    private HashMap<String, String> headers = new HashMap<>();

    public int getStatus() {
        return statusCode;
    }

    public void setStatus(int status) {
        this.statusCode = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}
