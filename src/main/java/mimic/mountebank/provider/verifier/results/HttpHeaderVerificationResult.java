package mimic.mountebank.provider.verifier.results;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpHeaderVerificationResult {

    private int contractStatusCode;
    private int providerStatusCode;
    private Map<String, String> contractHeaders;
    private Map<String, String> providerHeaders;
    private ReportStatus reportStatus;

    public int getContractStatusCode() {
        return contractStatusCode;
    }

    public void setContractStatusCode(int contractStatusCode) {
        this.contractStatusCode = contractStatusCode;
    }

    public int getProviderStatusCode() {
        return providerStatusCode;
    }

    public void setProviderStatusCode(int providerStatusCode) {
        this.providerStatusCode = providerStatusCode;
    }

    public Map<String, String> getContractHeaders() {
        return contractHeaders;
    }

    public void setContractHeaders(Map<String, String> contractHeaders) {
        this.contractHeaders = contractHeaders;
    }

    public Map<String, String> getProviderHeaders() {
        return providerHeaders;
    }

    public void setProviderHeaders(Map<String, String> providerHeaders) {
        this.providerHeaders = providerHeaders;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public List<String> headerDiff() {
        List<String> diff = new ArrayList<>();

        if (contractHeaders != null) {
            diff.addAll(contractHeaders.keySet());
        }
        if (providerHeaders != null) {
            diff.removeAll(providerHeaders.keySet());
        }

        return diff;
    }

    public LinkedList<DiffMatchPatch.Diff> statusDiff() {
        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(providerStatusCode+"", contractStatusCode+"");
        dmp.diffCleanupSemantic(diff);
        return diff;
    }
}
