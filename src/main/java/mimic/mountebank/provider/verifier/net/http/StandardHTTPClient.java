package mimic.mountebank.provider.verifier.net.http;

import mimic.mountebank.imposter.HttpPredicate;
import mimic.mountebank.net.http.HttpMethod;
import mimic.mountebank.provider.ProviderResponse;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    @Override
    public ProviderResponse sendRequest(String baseUrl, HttpPredicate httpPredicate) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        // add path parameters to URL if any
        HttpUrl httUrl = createUrl(baseUrl, httpPredicate);

        // add headers if any
        Headers headers = createHeaders(httpPredicate);

        // create request builder
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httUrl);
        requestBuilder.headers(headers);

        // if request body is present, add it to the request builder
        RequestBody body = null;
        if (httpPredicate.getBody() != null) {
            body = RequestBody.create(
                    null,
                    httpPredicate.getBody()
            );

        }

        // set the request method
        setHttpMethod(requestBuilder, httpPredicate, body);

        // build the request
        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            logRequest(httpPredicate.getMethod(), httUrl, headers, body);
            if (response.isSuccessful()) {

                return createResponse(response);

            } else {

//                String bodyStr = response.body().string();
//                String msg = bodyStr != null && bodyStr != "" ? bodyStr : "Unable to POST imposter to Mountebank";
//                throw new MountebankCommunicationException(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ProviderResponse createResponse(Response response) throws IOException {
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

        return new ProviderResponse(
                response.code(),
                mediaType,
                responseHeaders,
                response.body().string()
        );
    }

    private void setHttpMethod(Request.Builder requestBuilder, HttpPredicate httpPredicate, RequestBody body) {
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
        // add path parameters to URL if any
        String url = baseUrl+httpPredicate.getPath();
        HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();
        for (Map.Entry<String, String> queryParam : httpPredicate.getQueries().entrySet()) {
            httpBuider.addQueryParameter(queryParam.getKey(), queryParam.getValue());
        }

        return httpBuider.build();
    }

    private void logRequest(HttpMethod httpMethod, HttpUrl httpUrl, Headers headers, RequestBody body) {
        String strBody = body != null ? body.toString() : "No body";
        String strHeaders = headers.size() > 0 ? headers.toString() : "No headers";

        logger.info("\nSending {} request\nto URL: {}\nwith headers:\n{}\nand body:\n{}",
                httpMethod.toString(),
                httpUrl.toString(),
                strHeaders,
                strBody);
    }
}
