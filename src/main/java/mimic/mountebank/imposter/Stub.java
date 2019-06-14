package mimic.mountebank.imposter;


import java.util.ArrayList;
import java.util.List;

public class Stub {

    private List<Equals> predicates = new ArrayList<>();
    private List<Responses> responses = new ArrayList<>();

    public List<Responses> getResponses() {
        return responses;
    }

    public void addResponse(Responses responses) {
        this.responses.add(responses);
    }

    public List<Equals> getPredicates() {
        return predicates;
    }


    public void addPredicate(Equals predicate) {
        this.predicates.add(predicate);
    }
}
