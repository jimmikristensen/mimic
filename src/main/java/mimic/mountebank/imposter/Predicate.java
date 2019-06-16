package mimic.mountebank.imposter;

import java.util.ArrayList;
import java.util.List;

public class Predicate {

    EqualsParams equals;

//    public void addEquals(EqualsParams equals) {
//        this.equals.add(equals);
//    }

    public EqualsParams getEquals() {
        return equals;
    }

    public void setEquals(EqualsParams equals) {
        this.equals = equals;
    }
}
