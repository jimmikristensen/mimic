package mimic

import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.net.http.MountebankClient
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import spock.lang.Specification


class MountebankIntegrationSpec extends Specification {

    def "posting the imposter to mountebank from the DSL is successful"() {
        given:
        def container = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .path("/test")
                    .method(HttpMethod.GET)
                .expectResponse()
                    .status(201)
                .toMountebank()

        when:
        def stubUrl = "http://localhost:${container.getMappedPort(4321)}"
        Response res = sendGetToStub("${stubUrl}/test")

        then:
        res.code() == 201

        and:
        new MountebankClient(container).deleteAllImposters()
    }

    private Response sendGetToStub(String url) {
        OkHttpClient client = new OkHttpClient()
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build()

        return client.newCall(request).execute()
    }

}