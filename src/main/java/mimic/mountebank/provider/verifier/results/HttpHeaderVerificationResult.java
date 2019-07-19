package mimic.mountebank.provider.verifier.results;

import mimic.mountebank.provider.verifier.results.diff.Diff;
import mimic.mountebank.provider.verifier.results.diff.DiffOperation;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class HttpHeaderVerificationResult implements VerificationResult {

    private int contractStatusCode;
    private int providerStatusCode;
    private Map<String, String> contractHeaders;
    private Map<String, String> providerHeaders;
    private ReportStatus reportStatus;

    final Logger logger = LoggerFactory.getLogger(HttpHeaderVerificationResult.class);

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

    public List<Diff> getDiff() {
        getStatusDiff();
        getHeaderDiff();
        return null;
    }

    private List<Diff> getHeaderDiff() {
        System.out.println(contractHeaders);
        System.out.println(providerHeaders);

        Map<String, String> exactMatch = new HashMap<>();
        Map<String, String> valueMismatch = new HashMap<>();
        Map<String, String> missing = new HashMap<>();

        for (Map.Entry<String, String> contractHeader : contractHeaders.entrySet()) {
            String ck = contractHeader.getKey();
            String cv = contractHeader.getValue();

            exactMatch = providerHeaders.entrySet().stream()
                    .filter(ph -> ph.getKey().equals(ck) && ph.getValue().equals(cv))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            valueMismatch = providerHeaders.entrySet().stream()
                    .filter(ph -> ph.getKey().equals(ck) && !ph.getValue().equals(cv))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            missing = providerHeaders.entrySet().stream()
                    .filter(ph -> !ph.getKey().equals(ck))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }


//        List<String> contractRetainedKeys = new ArrayList<>(contractHeaders.keySet());
//        contractRetainedKeys.retainAll(providerHeaders.entrySet());
//
//        List<String> providerRetainedKeys = new ArrayList<>(providerHeaders.keySet());
//        providerRetainedKeys.retainAll(contractHeaders.entrySet());

        System.out.println(exactMatch);
        System.out.println(valueMismatch);
        System.out.println(missing);

        return null;
    }

    private List<Diff> getStatusDiff() {
        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(providerStatusCode+"", contractStatusCode+"");
        dmp.diffCleanupSemantic(diff);

        logger.info(diff.toString());

        List<Diff> diffResult = new LinkedList<>();

        diff.forEach((n) -> {
            diffResult.add(new Diff(
                    DiffOperation.getEnum(n.operation.toString().toUpperCase()),
                    "statusCode",
                    n.text,
                    null
            ));
        });

        return diffResult;
    }
}
