package mimic.mountebank.provider.verifier.results.diff;

import java.util.*;

public class DiffSet {

    public enum Type {
        BODY,
        STATUS_CODE,
        HEADER
    }

    public static Map<DiffSet.Type, List<Diff>> diffSets = new HashMap<>();

    public static void add(DiffSet.Type type, Diff diff) {
        List<Diff> diffList = diffSets.get(type);

        if (diffList == null) {
            diffList = new LinkedList<>();
            diffSets.put(type, diffList);
        }

        diffList.add(diff);
    }

    public static void add(DiffSet.Type type, Diff.Type diffType, DiffOperation op, String path, String value, String from) {
        add(type, new Diff(diffType, op, path, value, from));
    }

    public static List<Diff> get(DiffSet.Type type) {
        return diffSets.get(type) != null ? diffSets.get(type) : new LinkedList<>();
    }

    public static Map<DiffSet.Type, List<Diff>> get() {
        return diffSets;
    }

    public static Set<Type> getTypes() {
        return diffSets.keySet();
    }

    public static void clear() {
        diffSets.clear();
    }

    public static String toDiffString() {
        return "DiffSet{"+diffSets+"}";
    }
}
