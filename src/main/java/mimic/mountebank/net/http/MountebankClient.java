package mimic.mountebank.net.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mimic.mountebank.MountebankContainer;
import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.net.http.exception.ImposterParseException;
import mimic.mountebank.net.http.exception.MountebankCommunicationException;
import mimic.mountebank.net.http.exception.InvalidImposterURLException;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MountebankClient {

    private String mbManagementUrl;

    public MountebankClient(MountebankContainer mbContainer) {
        mbManagementUrl = "http://localhost:"+mbContainer.getMappedPort(2525);
    }

    public MountebankClient(String mbManagementUrl) {
        this.mbManagementUrl = mbManagementUrl;
    }

    public boolean postImposter(String imposter) throws IOException {
        String impostersUrl = mbManagementUrl+"/imposters";

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                imposter
        );

        Request request = new Request.Builder()
                .url(impostersUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                String bodyStr = response.body().string();
                String msg = bodyStr != null && bodyStr != "" ? bodyStr : "Unable to POST imposter to Mountebank";
                throw new MountebankCommunicationException(msg);
            }
        }
    }

    public boolean deleteAllImposters() throws IOException {
        String impostersUrl = mbManagementUrl+"/imposters";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(impostersUrl)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                String bodyStr = response.body().string();
                String msg = bodyStr != null && bodyStr != "" ? bodyStr : "Unable to delete imposters using URL: "+impostersUrl;
                throw new MountebankCommunicationException(msg);
            }
        }

    }

    public boolean deleteImposter(int imposterPort) throws IOException {
        String impostersUrl = mbManagementUrl+"/imposters/"+imposterPort;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(impostersUrl)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                String bodyStr = response.body().string();
                String msg = bodyStr != null && bodyStr != "" ? bodyStr : "Unable to delete imposter on port "+impostersUrl+" using URL: "+impostersUrl;
                throw new MountebankCommunicationException(msg);
            }
        }
    }

    public List<Imposter> getImposters() throws IOException {
        String impostersUrl = mbManagementUrl+"/imposters";

        List<Imposter> imposters = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(impostersUrl)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String bodyStr = response.body().string();
                String msg = bodyStr != null && bodyStr != "" ? bodyStr : "Unable to fetch imposter list on URL: "+impostersUrl;
                throw new MountebankCommunicationException(msg);
            }

            String body = response.body().string();
            ObjectMapper mapper = JacksonObjectMapper.getMapper();

            try {
                JsonNode imposterNodes = mapper.readTree(body).get("imposters");
                if (imposterNodes.isArray()) {
                    for (JsonNode imposterNode : imposterNodes) {
                        String imposterUrl = imposterNode.get("_links").get("self").get("href").asText();
                        imposters.add(getImposter(imposterUrl));
                    }
                }
            } catch (NullPointerException e) {
                throw new ImposterParseException("Unable to parse imposter response", e);
            }
        }
        return imposters;
    }

    public Imposter getImposter(int imposterPort) throws IOException {
        String impostersUrl = mbManagementUrl+"/imposters/"+imposterPort;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(impostersUrl)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String body = response.body().string();
                ObjectMapper mapper = JacksonObjectMapper.getMapper();
                return mapper.readValue(body, Imposter.class);
            } else {
                String bodyStr = response.body().string();
                String msg = bodyStr != null && bodyStr != "" ? bodyStr : "Unable to fetch imposter on port "+impostersUrl+" using URL: "+impostersUrl;
                throw new MountebankCommunicationException(msg);
            }
        }
    }

    private Imposter getImposter(String imposterUrl) throws IOException {
        URL url = new URL(imposterUrl);

        try {
            // extract the port number by removing all non-digits
            String portString = url.getPath().replaceAll("\\D+", "");
            int port = Integer.parseInt(portString);
            return getImposter(port);
        } catch (NumberFormatException e) {
            throw new InvalidImposterURLException("Unable to parse imposter port number", e);
        }
    }

}