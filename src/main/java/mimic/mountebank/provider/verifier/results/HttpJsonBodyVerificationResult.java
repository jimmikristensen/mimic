package mimic.mountebank.provider.verifier.results;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.fge.jsonpatch.diff.JsonDiff;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.provider.verifier.results.diff.Diff;
import mimic.mountebank.provider.verifier.results.diff.DiffOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpJsonBodyVerificationResult extends BodyVerificationResult {

    final Logger logger = LoggerFactory.getLogger(HttpJsonBodyVerificationResult.class);

    public List<Diff> getDiff() {
        try {

            providerBody = providerBody != null ? providerBody : "";
            contractBody = contractBody != null ? contractBody : "";

            ObjectMapper mapper = JacksonObjectMapper.getMapper();

            JsonNode providerNode = mapper.readTree(providerBody);
            JsonNode contractNode = mapper.readTree(contractBody);

            if (providerNode == null) {
                providerNode = mapper.createObjectNode();
            }

            if (contractNode == null) {
                contractNode = mapper.createObjectNode();
            }

            JsonNode patch = JsonDiff.asJson(providerNode, contractNode);

            logger.info(patch.toString());

            List<Diff> diff = new LinkedList<>();

            patch.forEach(n -> {
                String diffOperationName = n.get("op").asText().toUpperCase().trim();
                DiffOperation op = DiffOperation.getEnum(diffOperationName);
                String diffBranch = n.get("path").asText();
                String diffValue = null;
                String diffFrom = null;

                if (n.get("from") != null) {
                    diffFrom = n.get("from").asText();
                }

                if (n.get("value") != null) {
                    diffValue = n.get("value").asText();
                }

                diff.add(new Diff(
                        op,
                        diffBranch,
                        diffValue,
                        diffFrom
                ));

            });

            return diff;

        } catch (IOException e) {
            logger.error("Unable to serialize json string", e);
            throw new UncheckedIOException(e);
        }
    }
}
