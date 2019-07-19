package mimic.mountebank.provider.verifier.results;

public abstract class BodyVerificationResult implements VerificationResult {

    protected ReportStatus reportStatus;
    protected String contractBody;
    protected String providerBody;

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getContractBody() {
        return contractBody;
    }

    public void setContractBody(String contractBody) {
        this.contractBody = contractBody;
    }

    public String getProviderBody() {
        return providerBody;
    }

    public void setProviderBody(String providerBody) {
        this.providerBody = providerBody;
    }

}
