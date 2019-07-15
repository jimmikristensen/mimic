package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.Imposter;

import java.util.ArrayList;
import java.util.List;

public class ContractVerifier {

    private VerificationFactory verificationFactory;

    public ContractVerifier(VerificationFactory verificationFactory) {
        this.verificationFactory = verificationFactory;
    }

    public boolean verify(Imposter imposter) {
        List<Imposter> imposters = new ArrayList<>();
        imposters.add(imposter);
        return verify(imposters);
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



        for (Imposter imposter : imposters) {
            System.out.println(imposters.get(0).getProtocol());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getBody());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getHeaders());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getMethod());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getPath());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getQueries());
        }


        imposters.get(0).getProtocol();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getBody();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getHeaders();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getMethod();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getPath();
        imposters.get(0).getStub(0).getPredicate(0).getEquals().getQueries();

        MessageHeaderVerifier headerVerifier = verificationFactory.createHttpHeaderVerifier();
        return false;
    }
}
