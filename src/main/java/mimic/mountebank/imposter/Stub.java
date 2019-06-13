package mimic.mountebank.imposter;


import java.util.ArrayList;
import java.util.List;

public class Stub {

    private List<Equals> predicates = new ArrayList<>();
    private List<Response> responses = new ArrayList<>();

    public List<Response> getResponses() {
        return responses;
    }

    public void addResponse(Response response) {
        this.responses.add(response);
    }

    public List<Equals> getPredicates() {
        return predicates;
    }


    public void addPredicate(Equals predicate) {
        this.predicates.add(predicate);
    }
}
