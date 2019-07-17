package mimic.mountebank.provider.report;

import java.util.Map;

public interface ProviderReport {

    public void printReport();
    public String getReport();
    public void saveReport();

}
