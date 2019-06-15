package mimic.mountebank.net.http;

import okhttp3.*;

import java.io.IOException;

public class MountebankClient {

    public MountebankClient() {

    }

    public boolean postImposter(String imposter, String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                imposter
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.isSuccessful();
    }


}
