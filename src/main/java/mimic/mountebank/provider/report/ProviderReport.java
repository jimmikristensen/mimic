package mimic.mountebank.provider.report;

import java.util.Map;

public interface ProviderReport {

    public void printReport();
    public String toString();
    public Map<String, String> getProviderRequestEvents();
    public Map<String, String> getProviderResponseEvents();
    public void setProviderRequestEvents(Map<String, String> providerRequestEvents);
    public void setProviderResponseEvents(Map<String, String> providerResponseEvents);

}
