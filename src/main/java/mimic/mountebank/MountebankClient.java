package mimic.mountebank;

import okhttp3.*;

import java.io.IOException;

public class MountebankClient {

    public MountebankClient() {

    }

    public void writeImposter(String imposter, String url) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                imposter
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.code());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
