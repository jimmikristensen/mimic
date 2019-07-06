package mimic.mountebank.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import mimic.mountebank.consumer.dsl.ImposterBuilder;
import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.net.http.MountebankClient;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;

public class ConsumerImposterBuilder {

    private static Imposter imposter;

    public static ImposterBuilder Builder() {
        imposter = new Imposter();
        return new ImposterBuilder(imposter);
    }

    public static Imposter getImposter() {
        return imposter;
    }

    public static String getImposterAsJsonString() throws IOException {
        ObjectMapper mapper = JacksonObjectMapper.getMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(imposter);
    }

    public static GenericContainer postImposterToMountebank() throws IOException {
        GenericContainer mbc = new MountebankContainerBuilder().managementPort(2525).stubPort(imposter.getPort()).build();
        new MountebankClient(mbc).postImposter(getImposterAsJsonString());
        return mbc;
    }

    public static String postImposterToMountebank(String mbManagementUrl) throws IOException {
        new MountebankClient(mbManagementUrl).postImposter(getImposterAsJsonString());
        return mbManagementUrl;
    }
}
