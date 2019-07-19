package mimic.mountebank.provider.verifier.results;

import mimic.mountebank.provider.verifier.results.diff.Diff;

import java.util.List;

public interface VerificationResult {

    public ReportStatus getReportStatus();
    public List<Diff> getDiff();

}
