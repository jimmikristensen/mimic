package mimic.mountebank.imposter;

import mimic.mountebank.net.http.HttpMethod;

import java.util.HashMap;

public interface HttpPredicate {

    public HttpMethod getMethod();
    public String getMethodAsString();
    public void setMethod(String method);
    public void setMethod(HttpMethod method);
    public String getPath();
    public void setPath(String path);
    public HashMap<String, String> getQueries();
    public void addQuery(String key, String value);
    public HashMap<String, String> getHeaders();
    public String getHeader(String key);
    public void addHeader(String key, String value);
    public String getBody();
    public void setBody(String body);
}
