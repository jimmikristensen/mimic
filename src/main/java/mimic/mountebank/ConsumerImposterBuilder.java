package mimic.mountebank;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import mimic.mountebank.fragment.PredicateBuilder;
import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.imposter.Stub;

import java.io.IOException;

public class ConsumerImposterBuilder {

    private Imposter imposter;

    public ConsumerImposterBuilder(int port, String protocol) {
        imposter = new Imposter();
        imposter.setPort(port);
        imposter.setProtocol(protocol.toLowerCase());
    }

    public PredicateBuilder givenRequest() {
        Stub stub = new Stub();
        imposter.addStubs(stub);

        return new PredicateBuilder(stub);
    }

    public Imposter getImposter() {
        return imposter;
    }

    public String getImposterAsJsonString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(imposter);
    }
}
