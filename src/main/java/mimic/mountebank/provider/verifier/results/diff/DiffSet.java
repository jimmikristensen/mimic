package mimic.mountebank.provider.verifier.results.diff;

import java.util.*;

public class DiffSet {

    public enum Type {
        BODY,
        STATUS_CODE,
        HEADER
    }

    public Map<DiffSet.Type, List<Diff>> diffSets = new HashMap<>();

    public void add(DiffSet.Type type, Diff diff) {
        List<Diff> diffList = diffSets.get(type);

        if (diffList == null) {
            diffList = new LinkedList<>();
            diffSets.put(type, diffList);
        }

        diffList.add(diff);
    }

    public void add(DiffSet.Type type, Diff.Type diffType, DiffOperation op, String path, String value, String from) {
        add(type, new Diff(diffType, op, path, value, from));
    }

    public List<Diff> get(DiffSet.Type type) {
        return diffSets.get(type);
    }

    public Set<Type> getTypes() {
        return diffSets.keySet();
    }
}
