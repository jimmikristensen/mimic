package mimic.mountebank.imposter;

import java.util.ArrayList;
import java.util.List;

public class Responses {

    private List<Response> is = new ArrayList<>();

    public List<Response> getIs() {
        return is;
    }

    public void addResponse(Response is) {
        this.is.add(is);
    }
}
