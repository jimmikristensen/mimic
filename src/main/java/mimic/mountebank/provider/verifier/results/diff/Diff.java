package mimic.mountebank.provider.verifier.results.diff;

public class Diff {

    private final Diff.Type diffType;
    private final DiffOperation op;
    private final String diffPath;
    private final String diffValue;
    private final String diffFrom;

    public enum Type {
        JSON,
        TEXT
    }

    public Diff(Diff.Type diffType, DiffOperation op, String path, String value, String from) {
        this.diffType = diffType;
        this.op = op;
        this.diffPath = path;
        this.diffValue = value;
        this.diffFrom = from;
    }

    public Diff(Diff.Type diffType, DiffOperation op, String path) {
        this.diffType = diffType;
        this.op = op;
        this.diffPath = path;
        diffValue = null;
        diffFrom = null;
    }

    public Diff.Type getType() {
        return diffType;
    }

    public DiffOperation getOperation() {
        return op;
    }

    public String getPath() {
        return diffPath;
    }

    public String getValue() {
        return diffValue;
    }

    public String getFrom() {
        return diffFrom;
    }

    @Override
    public String toString() {
        return "Diff{" +
                "op=" + op +
                ", path='" + diffPath + '\'' +
                ", value='" + diffValue + '\'' +
                ", from='" + diffFrom + '\'' +
                '}';
    }
}
