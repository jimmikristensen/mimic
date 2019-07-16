package mimic.mountebank.provider.verifier.results;

import mimic.mountebank.net.http.HttpMethod;

import java.util.LinkedHashMap;
import java.util.Map;

public class StandardHTTPResult implements HTTPResult {

    private HttpMethod httpMethod;
    private String url = "";
    private Map<String, String> requestHeaders = new LinkedHashMap<>();
    private String requestBody = "";
    private int status = 0;
    private Map<String, String> responseHeaders = new LinkedHashMap<>();
    private String responseBody = "";
    private String responseMediaType = "";

    @Override
    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    @Override
    public String getRequestUrl() {
        return url;
    }

    @Override
    public void setRequestUrl(String url) {
        this.url = url;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public void addRequestHeader(String key, String value) {
        requestHeaders.put(key, value);
    }

    @Override
    public void setRequestHeaders(Map<String, String> headers) {
        this.requestHeaders = headers;
    }

    @Override
    public String getRequestBody() {
        return requestBody;
    }

    @Override
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public int getResponseStatus() {
        return status;
    }

    @Override
    public void setResponseStatus(int status) {
        this.status = status;
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public void addResponseHeader(String key, String value) {
        responseHeaders.put(key, value);
    }

    @Override
    public void setResponseHeaders(Map<String, String> headers) {
        this.responseHeaders = headers;
    }

    @Override
    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public void setResponseMediaType(String mediaType) {
        this.responseMediaType = mediaType;
    }

    @Override
    public String getResponseMediaType() {
        return responseMediaType;
    }
}
