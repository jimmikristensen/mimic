package mimic.mountebank.imposter;

import java.util.ArrayList;
import java.util.List;

public class Imposter {

    public int port;
    public String protocol;
    public List<Stub> stubs = new ArrayList<>();
}
