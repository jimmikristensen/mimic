package mimic.mountebank.imposter;


import java.util.ArrayList;
import java.util.List;

public class Stub {

    private List<Equals> predicates = new ArrayList<>();

    public List<Equals> getPredicates() {
        return predicates;
    }

    public void addPredicate(Equals predicate) {
        this.predicates.add(predicate);
    }
}
