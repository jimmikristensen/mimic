package mimic.mountebank.imposter;

public class Response {

    private ResponseFields is = new ResponseFields();

    public ResponseFields getFields() {
        return is;
    }

    public void setFields(ResponseFields fields) {
        this.is = fields;
    }
}
