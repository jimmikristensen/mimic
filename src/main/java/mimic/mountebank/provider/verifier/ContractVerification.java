package mimic.mountebank.provider.verifier;

public class ContractVerification implements ContractVerificationFactory {

    @Override
    public HttpHeaderVerifier createrHttpHeaderVerifier() {
        return new StandardHttpHeaderVerifier();
    }
}
