package mimic.mountebank.provider.verifier.results;


import mimic.mountebank.net.http.HttpMethod;

import java.util.Map;

public interface HTTPResult {

    public HttpMethod getHttpMethod();
    public void setHttpMethod(HttpMethod httpMethod);
    public String getRequestUrl();
    public void setRequestUrl(String url);
    public Map<String, String> getRequestHeaders();
    public void addRequestHeader(String key, String value);
    public void setRequestHeaders(Map<String, String> headers);
    public String getRequestBody();
    public void setRequestBody(String requestBody);

    public int getResponseStatus();
    public void setResponseStatus(int status);
    public Map<String, String> getResponseHeaders();
    public void addResponseHeader(String key, String value);
    public void setResponseHeaders(Map<String, String> headers);
    public String getResponseBody();
    public void setResponseBody(String responseBody);
}
