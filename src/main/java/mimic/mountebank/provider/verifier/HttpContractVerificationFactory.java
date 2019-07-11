package mimic.mountebank.provider.verifier;

public class HttpContractVerificationFactory implements ContractVerificationFactory {

    @Override
    public HttpHeaderVerifier createHttpHeaderVerifier() {
        return new StandardHttpHeaderVerifier();
    }
}
