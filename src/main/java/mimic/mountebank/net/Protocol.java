package mimic.mountebank.net;

public enum Protocol {

    HTTP ("http"),
    HTTPS ("https");

    private final String protocolString;

    private Protocol(String protocolString) {
        this.protocolString = protocolString;
    }

    public String getProtocol() {
        return protocolString;
    }

}
