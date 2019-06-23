package mimic.mountebank.net.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mimic.mountebank.net.http.exception.InvalidImposterException;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                String bodyStr = response.body().string();
                String msg = bodyStr != null && bodyStr != "" ? bodyStr : "Unable to POST imposter to Mountebank";
                throw new InvalidImposterException(msg);
            }
        }
    }

    public boolean deleteAllImposters(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    public List<String> getImposters(String url) throws IOException {
        List<String> imposterUrls = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode imposterNodes = mapper.readTree(body).get("imposters");
            if (imposterNodes.isArray()) {
                for (JsonNode imposterNode : imposterNodes) {
                    imposterUrls.add(imposterNode.asText());
                }
            }
        }

        return imposterUrls;
    }

    public String getImposter(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}
