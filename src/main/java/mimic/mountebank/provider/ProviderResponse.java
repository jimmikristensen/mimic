package mimic.mountebank.provider;

import java.util.Map;

public class ProviderResponse {

    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;
    private final String mediaType;

    public ProviderResponse(int statusCode, String mediaType, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.mediaType = mediaType;
    }

    public int getStatus() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMediaType() {
        return mediaType;
    }
}
