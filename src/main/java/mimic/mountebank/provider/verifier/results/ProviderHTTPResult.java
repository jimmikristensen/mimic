package mimic.mountebank.provider.verifier.results;

import mimic.mountebank.net.http.HttpMethod;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProviderHTTPResult {

    private HttpMethod httpMethod;
    private String url = "";
    private Map<String, String> requestHeaders = new LinkedHashMap<>();
    private String requestBody = "";
    private int status = 0;
    private Map<String, String> responseHeaders = new LinkedHashMap<>();
    private String responseBody = "";
    private String responseMediaType = "";

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getRequestUrl() {
        return url;
    }

    public void setRequestUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void addRequestHeader(String key, String value) {
        requestHeaders.put(key, value);
    }

    public void setRequestHeaders(Map<String, String> headers) {
        this.requestHeaders = headers;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public int getResponseStatus() {
        return status;
    }

    public void setResponseStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void addResponseHeader(String key, String value) {
        responseHeaders.put(key, value);
    }

    public void setResponseHeaders(Map<String, String> headers) {
        this.responseHeaders = headers;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setResponseMediaType(String mediaType) {
        this.responseMediaType = mediaType;
    }

    public String getResponseMediaType() {
        return responseMediaType;
    }
}
