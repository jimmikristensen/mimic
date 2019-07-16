package mimic.mountebank.provider.report;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProviderReportBuilder {

    private final Map<String, String> providerRequestEvents = new LinkedHashMap<>();
    private final Map<String, String> providerResponseEvents = new LinkedHashMap<>();
    private final ProviderReport report;

    public ProviderReportBuilder(ProviderReport report) {
        this.report = report;
    }

    public ProviderReportBuilder() {
        report = new StandardProviderReport();
    }

    public ProviderReportBuilder createProviderRequestEvent(String key, String event) {
        providerRequestEvents.put(key, event);
        return this;
    }

    public ProviderReportBuilder createProviderResponseEvent(String key, String event) {
        providerResponseEvents.put(key, event);
        return this;
    }

    public ProviderReport build() {
        report.setProviderRequestEvents(providerRequestEvents);
        report.setProviderResponseEvents(providerResponseEvents);
        return report;
    }
}
