package mimic.mountebank.provider;

import java.util.HashMap;

public class ProviderResponse {

    private int statusCode;
    private String body;
    private HashMap<String, String> headers = new HashMap<>();

    public int getStatus() {
        return statusCode;
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
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

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }
}
