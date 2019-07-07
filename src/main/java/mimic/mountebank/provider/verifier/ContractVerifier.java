package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.Imposter;

import java.util.List;

public class ContractVerifier {

    private ContractVerificationFactory contractVerification;

    public ContractVerifier(ContractVerificationFactory contractVerification) {
        this.contractVerification = contractVerification;
    }

    public boolean verify(List<Imposter> imposters) {
        // http call to provider based in imposter contract
        // compare with contract by looping them

        /*
        Given provider
            <Provider>
        When
            HTTP <Method> request is send to <URL>
        Then response contains
            status code <code>
            headers
                <list of headers>
            a matching body
         */


        imposters.get(0).getProtocol();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getBody();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getHeaders();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getMethod();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getPath();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getQueries();

        HttpHeaderVerifier headerVerifier = contractVerification.createrHttpHeaderVerifier();

    }
}
