package mimic.mountebank.provider.verifier.results;

import mimic.mountebank.provider.verifier.results.diff.Diff;
import mimic.mountebank.provider.verifier.results.diff.DiffOperation;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class HttpTextBodyVerificationResult extends BodyVerificationResult {

    final Logger logger = LoggerFactory.getLogger(HttpTextBodyVerificationResult.class);

    public List<Diff> getDiff() {
        String providerBodyStr = providerBody != null ? providerBody : "";
        String contractBodyStr = contractBody != null ? contractBody : "";

        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(providerBodyStr, contractBodyStr);
        dmp.diffCleanupSemantic(diff);

        List<Diff> diffResult = new LinkedList<>();

        diff.forEach((n) -> {
           diffResult.add(new Diff(
                   Diff.Type.TEXT,
                    DiffOperation.getEnum(n.operation.toString().toUpperCase()),
                    null,
                   n.text,
                    null
            ));
        });

        logger.info(diffResult.toString());

        return diffResult;
    }
}
