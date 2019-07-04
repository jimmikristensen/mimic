package mimic.consumer

import mimic.mountebank.ConsumerImposterBuilder
import mimic.mountebank.MountebankContainerBuilder
import mimic.mountebank.net.http.HttpMethod
import mimic.mountebank.net.http.MountebankClient
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.testcontainers.containers.GenericContainer
import spock.lang.Specification


class MountebankIntegrationSpec extends Specification {

    def "posting the imposter to mountebank from the DSL is successful"() {
        given:
        GenericContainer container = ConsumerImposterBuilder.Builder()
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

    def "posting the imposter to remote mountebank from the DSL is successful"() {
        given:
        def remoteMountebank = new MountebankContainerBuilder().managementPort(2525).stubPort(4321).build()

        when:
        String mbManagementUrl = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                    .equals()
                    .path("/test")
                    .method(HttpMethod.GET)
                .expectResponse()
                    .status(201)
                .toMountebank("http://localhost:${remoteMountebank.getMappedPort(2525)}")

        and:
        def stubUrl = "http://localhost:${remoteMountebank.getMappedPort(4321)}"
        Response res = sendGetToStub("${stubUrl}/test")

        then:
        res.code() == 201
    }

    def "attempting to post imposter to mountebank when it is not running results in ConnectException"() {
        when:
        String mbManagementUrl = ConsumerImposterBuilder.Builder()
                .givenRequest(4321)
                .equals()
                .path("/test")
                .method(HttpMethod.GET)
                .expectResponse()
                .status(201)
                .toMountebank("http://localhost:5252")

        and:
        def stubUrl = "http://localhost:4321"
        Response res = sendGetToStub("${stubUrl}/test")

        then:
        ConnectException ex = thrown()
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