package mimic.mountebank.provider.verifier.results.diff;

public class Diff {

    private final DiffOperation op;
    private final String diffBranch;
    private final String diffValue;
    private final String diffFrom;

    public Diff(DiffOperation op, String diffBranch, String diffValue, String diffFrom) {
        this.op = op;
        this.diffBranch = diffBranch;
        this.diffValue = diffValue;
        this.diffFrom = diffFrom;
    }

    public Diff(DiffOperation op, String diffBranch) {
        this.op = op;
        this.diffBranch = diffBranch;
        diffValue = null;
        diffFrom = null;
    }

    public DiffOperation getOperation() {
        return op;
    }

    public String getBranch() {
        return diffBranch;
    }

    public String getValue() {
        return diffValue;
    }

    public String getFrom() {
        return diffFrom;
    }
}
