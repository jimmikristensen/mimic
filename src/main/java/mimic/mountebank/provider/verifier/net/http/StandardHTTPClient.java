package mimic.mountebank.provider.verifier.net.http;

import mimic.mountebank.imposter.HttpPredicate;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A HTTP client strategy for sending request to Provider API based on the data in the {@link HttpPredicate}.
 * The predicate is used as contract between the consumer and provider.
 * This particular strategy uses {@link OkHttpClient} for requests - implement the {@link HTTPClient} to create your
 * own strategy.
 *
 * @see <a target="_blank" href="https://square.github.io/okhttp/">okhttp</a>
 */
public class StandardHTTPClient implements HTTPClient {

    final Logger logger = LoggerFactory.getLogger(StandardHTTPClient.class);
    private ProviderHTTPResult httpResult;

    public StandardHTTPClient() {
        this.httpResult = new ProviderHTTPResult();
    }

    @Override
    public ProviderHTTPResult sendRequest(String baseUrl, HttpPredicate httpPredicate) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        HttpUrl httUrl = createUrl(baseUrl, httpPredicate);
        System.out.println("-----------");
        System.out.println(httUrl.toString());
        Headers headers = createHeaders(httpPredicate);
        RequestBody body = createBody(httpPredicate);

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httUrl);
        requestBuilder.headers(headers);
        setHttpMethod(requestBuilder, httpPredicate, body);
        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            return createResponse(response);

        } catch (IOException e) {
            logger.error("Unable to execute HTTP request to provider server", e);
            throw new UncheckedIOException(e);
        }
    }

    private ProviderHTTPResult createResponse(Response response) throws IOException {
        Map<String, String> responseHeaders = new HashMap<>();

        for (String name : response.headers().names()) {
            responseHeaders.put(
                    name,
                    response.headers().get(name)
            );
        }

        String mediaType = null;
        if (response.header("Content-Type") != null) {
            mediaType = MediaType.parse(response.header("Content-Type")).toString();
        }

        String responseBody = response.body().string();

        httpResult.setResponseStatus(response.code());
        httpResult.setResponseHeaders(responseHeaders);
        httpResult.setResponseBody(responseBody);
        httpResult.setResponseMediaType(mediaType);

        return httpResult;
    }

    private void setHttpMethod(Request.Builder requestBuilder, HttpPredicate httpPredicate, RequestBody body) {
        try {
            httpResult.setHttpMethod(httpPredicate.getMethod());
            switch (httpPredicate.getMethod()) {
                case GET:
                    requestBuilder.get();
                    break;
                case HEAD:
                    requestBuilder.head();
                    break;
                case POST:
                    requestBuilder.post(body);
                    break;
                case DELETE:
                    requestBuilder.delete(body);
                    break;
                case PATCH:
                    requestBuilder.patch(body);
                    break;
                case PUT:
                    requestBuilder.put(body);
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Request HTTP Method must be specified", e);
        }
    }

    private RequestBody createBody(HttpPredicate httpPredicate) {
        RequestBody body = null;
        if (httpPredicate.getBody() != null) {
            body = RequestBody.create(
                    null,
                    httpPredicate.getBody()
            );
            httpResult.setRequestBody(requestBodyToString(body));
        }
        return body;
    }

    /**
     * Creates a {@link Headers} object containing headers from the {@link HttpPredicate} object.
     *
     * @param httpPredicate a {@link HttpPredicate} object
     * @return a {@link Headers} object containing all headers from {@link HttpPredicate}
     */
    private Headers createHeaders(HttpPredicate httpPredicate) {

        // add headers if any
        Headers.Builder headerBuilder = new Headers.Builder();
        for (Map.Entry<String, String> header : httpPredicate.getHeaders().entrySet()) {
            headerBuilder.add(header.getKey(), header.getValue());
            httpResult.addRequestHeader(header.getKey(), header.getValue());
        }

        return headerBuilder.build();
    }

    /**
     * Assembles a complete URL with query parameters from the {@link HttpPredicate}.
     *
     * @param baseUrl the base url
     * @param httpPredicate a {@link HttpPredicate} object
     * @return the assembled {@code URL}
     */
    private HttpUrl createUrl(String baseUrl, HttpPredicate httpPredicate) {
        try {
            String path = httpPredicate.getPath() != null ? httpPredicate.getPath() : "";
            String url = baseUrl + path;
            HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();
            for (Map.Entry<String, String> queryParam : httpPredicate.getQueries().entrySet()) {
                httpBuider.addQueryParameter(queryParam.getKey(), queryParam.getValue());
            }

            httpResult.setRequestUrl(httpBuider.toString());

            return httpBuider.build();

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Request URL must contain at least a base URL", e);
        }
    }

    private String requestBodyToString(RequestBody body) {
        String strBody = "";

        if (body != null) {
            try {
                Buffer buff = new Buffer();
                body.writeTo(buff);
                strBody = buff.readUtf8();
            } catch (IOException e) {
                logger.error("Request log was unable to write request body to buffer", e);
                throw new UncheckedIOException(e);
            }
        }

        return strBody;
    }
}
