package mimic.mountebank.imposter;


import java.util.ArrayList;
import java.util.List;

public class Stub {

    private List<Predicate> predicates = new ArrayList<>();
    private List<Response> responses = new ArrayList<>();

    public List<Response> getResponses() {
        return responses;
    }

    public Response getResponse(int index) {
        return responses.get(index);
    }

    public void addResponse(Response responses) {
        this.responses.add(responses);
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public Predicate getPredicate(int index) {
        return predicates.get(index);
    }

    public void addPredicate(Predicate predicate) {
        this.predicates.add(predicate);
    }
}
