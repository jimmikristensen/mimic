package mimic.mountebank.provider.verifier.net.http;

import mimic.mountebank.imposter.HttpPredicate;
import mimic.mountebank.net.http.HttpMethod;
import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StandardHTTPClient implements HTTPClient {

    @Override
    public void sendRequest(String baseUrl, HttpPredicate httpPredicate) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        String url = baseUrl+httpPredicate.getPath();
        HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();
        for (Map.Entry<String, String> queryParam : httpPredicate.getQueries().entrySet()) {
            httpBuider.addQueryParameter(queryParam.getKey(), queryParam.getValue());
        }
        HttpUrl httUrl = httpBuider.build();

        Headers.Builder headerBuilder = new Headers.Builder();
        for (Map.Entry<String, String> header : httpPredicate.getHeaders().entrySet()) {
            headerBuilder.add(header.getKey(), header.getValue());
        }
        Headers headers = headerBuilder.build();

        Request.Builder t = new Request.Builder();
        t.url(httUrl);
        t.headers(headers);

        switch (httpPredicate.getMethod()) {
            case GET:
                t.get();
            default:
                break;
        }


        RequestBody body = RequestBody.create(
                null,
                httpPredicate.getBody()
        );
    }
}
