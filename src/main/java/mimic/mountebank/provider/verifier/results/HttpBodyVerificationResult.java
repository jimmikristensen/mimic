package mimic.mountebank.provider.verifier.results;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.io.IOException;
import java.util.LinkedList;

public class HttpBodyVerificationResult {

    private ReportStatus reportStatus;
    private String contractBody;
    private String providerBody;

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getContractBody() {
        return contractBody;
    }

    public void setContractBody(String contractBody) {
        this.contractBody = contractBody;
    }

    public String getProviderBody() {
        return providerBody;
    }

    public void setProviderBody(String providerBody) {
        this.providerBody = providerBody;
    }

    public LinkedList<DiffMatchPatch.Diff> bodyDiff() {
        String providerBodyStr = providerBody != null ? providerBody : "";
        String contractBodyStr = contractBody != null ? contractBody : "";

        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(providerBodyStr, contractBodyStr);
        dmp.diffCleanupSemantic(diff);
        test();
        return diff;
    }

    public void test() {
        ObjectMapper mapper = new ObjectMapper();
        try {

            JsonNode providerNode = JacksonObjectMapper.getMapper().readTree(providerBody);
            JsonNode contractNode = JacksonObjectMapper.getMapper().readTree(contractBody);

            JsonPatch patch = JsonDiff.asJsonPatch(providerNode, contractNode);
            JsonNode patch2 = JsonDiff.asJson(providerNode, contractNode);

            //https://stackoverflow.com/questions/50967015/how-to-compare-json-documents-and-return-the-differences-with-jackson-or-gson
            //https://cassiomolin.com/2018/07/23/comparing-json-documents-in-java/
            //https://github.com/java-json-tools/json-patch

            System.out.println(patch2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
