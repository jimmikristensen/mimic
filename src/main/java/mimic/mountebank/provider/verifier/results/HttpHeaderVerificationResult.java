package mimic.mountebank.provider.verifier.results;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import mimic.mountebank.provider.verifier.results.diff.Diff;
import mimic.mountebank.provider.verifier.results.diff.DiffOperation;
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
        List<Diff> diffResult = Stream.of(getStatusDiff(), getHeaderDiff()).collect(LinkedList::new, List::addAll, List::addAll);

        logger.info(diffResult.toString());

        return diffResult;
    }

    private List<Diff> getHeaderDiff() {
        System.out.println(contractHeaders);
        System.out.println(providerHeaders);


        // find missing keys in
        Map<String, String> missingHeadersFromProvider = new HashMap<>(contractHeaders);
        missingHeadersFromProvider.keySet().removeAll(providerHeaders.keySet());

        System.out.println();
        System.out.println("missin headers: ");
        System.out.println(missingHeadersFromProvider);

        // key match but value mismatch
        Map<String, Map<String, String>> differentValues = contractHeaders.entrySet().stream()
                .filter(e -> !e.getValue().equals(providerHeaders.get(e.getKey())))
                .collect(Collectors.toMap(e -> e.getKey(), e -> Map.of("LEFT", e.getValue(), "RIGHT", providerHeaders.get(e.getKey()))));




        System.out.println("different values:");
        System.out.println(differentValues);

        System.out.println();

        System.out.println("Guava---");
        MapDifference<String, String> diff = Maps.difference(contractHeaders, providerHeaders);
        Map<String, MapDifference.ValueDifference<String>> entriesDiff = diff.entriesDiffering();

        System.out.println("missing headers: ");
        System.out.println(diff.entriesOnlyOnLeft());

        System.out.println("different values:");
        System.out.println(entriesDiff);
//        System.out.println(entriesDiff.get("Y-Header").leftValue());
//        System.out.println(entriesDiff.get("Y-Header").rightValue());


        List<Diff> diffResult = new LinkedList<>();

        missingHeadersFromProvider.forEach((k, v) -> {
            diffResult.add(new Diff(
                    DiffOperation.ADD,
                    "header",
                    v,
                    null
            ));
        });

        differentValues.forEach((k, v) -> {
            diffResult.add(new Diff(
                    DiffOperation.REPLACE,
                    "header",
                    v.get("LEFT"),
                    v.get("RIGHT")
            ));
        });

        System.out.println();
        System.out.println("DIFF");
        System.out.println(diffResult);

        diffResult.forEach(e -> {
            System.out.println(e.getOperation());
            System.out.println(e.getBranch());
            System.out.println(e.getValue());
            System.out.println(e.getFrom());
            System.out.println();
        });

        return diffResult;
    }

    private List<Diff> getStatusDiff() {
        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(providerStatusCode+"", contractStatusCode+"");
        dmp.diffCleanupSemantic(diff);

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
