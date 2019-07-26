package mimic.mountebank.provider.verifier.results;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import mimic.mountebank.provider.verifier.results.diff.Diff;
import mimic.mountebank.provider.verifier.results.diff.DiffOperation;
import mimic.mountebank.provider.verifier.results.diff.DiffSet;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        logger.info(DiffSet.toDiffString());
        return Stream.of(DiffSet.get(DiffSet.Type.STATUS_CODE), DiffSet.get(DiffSet.Type.HEADER)).collect(LinkedList::new, List::addAll, List::addAll);
    }

    private void getHeaderDiff() {
        // if no contract headers, no reason to check validation
        if (contractHeaders == null) return;

        providerHeaders = providerHeaders != null ? providerHeaders : new LinkedHashMap<>();

        // find missing keys
        Map<String, String> missingHeadersFromProvider = new HashMap<>(contractHeaders);
        missingHeadersFromProvider.keySet().removeAll(providerHeaders.keySet());

        // key match but value mismatch
        Map<String, Map<String, String>> differentValues = contractHeaders.entrySet().stream()
                .filter(e -> providerHeaders.get(e.getKey()) != null && !e.getValue().equals(providerHeaders.get(e.getKey())))
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> Map.of("LEFT", e.getValue(), "RIGHT", providerHeaders.get(e.getKey()))
                ));

        missingHeadersFromProvider.forEach((k, v) -> {
            DiffSet.add(DiffSet.Type.HEADER, new Diff(
                    Diff.Type.TEXT,
                    DiffOperation.ADD,
                    k,
                    v,
                    null
            ));
        });

        differentValues.forEach((k, v) -> {
            DiffSet.add(DiffSet.Type.HEADER, new Diff(
                    Diff.Type.TEXT,
                    DiffOperation.REPLACE,
                    k,
                    v.get("LEFT"),
                    v.get("RIGHT")
            ));
        });
    }

    private void getStatusDiff() {
        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(providerStatusCode+"", contractStatusCode+"");
        dmp.diffCleanupSemantic(diff);

        diff.forEach((n) -> {
            DiffSet.add(DiffSet.Type.STATUS_CODE, new Diff(
                    Diff.Type.TEXT,
                    DiffOperation.getEnum(n.operation.toString().toUpperCase()),
                    null,
                    n.text,
                    null
            ));
        });
    }
}
