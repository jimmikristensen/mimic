package mimic.mountebank.imposter;

import java.util.HashMap;

public class ImposterPredicate {

    public String method;
    public String path;
    public HashMap<String, String> queries = new HashMap<>();
    public HashMap<String, String> headers = new HashMap<>();
}
