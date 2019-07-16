package mimic.mountebank.provider.report;

import java.util.HashMap;
import java.util.Map;

public final class StandardProviderReport implements ProviderReport {

    private Map<String, String> providerRequestEvents;
    private Map<String, String> providerResponseEvents;

    public Map<String, String> getProviderRequestEvents() {
        return providerRequestEvents;
    }

    public Map<String, String> getProviderResponseEvents() {
        return providerResponseEvents;
    }

    public void setProviderRequestEvents(Map<String, String> providerRequestEvents) {
        this.providerRequestEvents = providerRequestEvents;
    }

    public void setProviderResponseEvents(Map<String, String> providerResponseEvents) {
        this.providerResponseEvents = providerResponseEvents;
    }

    public void printReport() {
        System.out.println(toString());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (!providerRequestEvents.isEmpty()) {
            sb.append(String.format("Sending request to provider%n"));
            for (Map.Entry<String, String> entry : providerRequestEvents.entrySet()) {
                sb.append(String.format("%s:%s%n", entry.getKey(), entry.getValue()));
            }
        }

        if (!providerResponseEvents.isEmpty()) {
            sb.append(String.format("Response received from provider%n"));
            for (Map.Entry<String, String> entry : providerResponseEvents.entrySet()) {
                sb.append(String.format("%s:%s%n", entry.getKey(), entry.getValue()));
            }
        }

        return sb.toString();
    }
}
