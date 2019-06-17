package mimic.mountebank;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import mimic.mountebank.fragment.ImposterBuilder;
import mimic.mountebank.imposter.Imposter;

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
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(imposter);
    }
}
