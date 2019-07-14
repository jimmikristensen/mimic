package mimic.mountebank.imposter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Predicate {

    @JsonDeserialize(as = Equals.class)
    HttpPredicate equals;

    public HttpPredicate getEquals() {
        return equals;
    }

    public void setEquals(Equals equals) {
        this.equals = equals;
    }
}
