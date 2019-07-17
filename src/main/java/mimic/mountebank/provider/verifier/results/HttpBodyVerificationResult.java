package mimic.mountebank.provider.verifier.results;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

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
        return diff;
    }
}
