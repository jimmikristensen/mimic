package mimic.mountebank.provider.verifier;

import mimic.mountebank.imposter.HttpPredicate;
import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.imposter.ResponseFields;
import mimic.mountebank.provider.verifier.net.http.HTTPClient;
import mimic.mountebank.provider.verifier.results.BodyVerificationResult;
import mimic.mountebank.provider.verifier.results.HttpTextBodyVerificationResult;
import mimic.mountebank.provider.verifier.results.HttpHeaderVerificationResult;
import mimic.mountebank.provider.verifier.results.ProviderHTTPResult;


import java.util.ArrayList;
import java.util.List;

public class ContractVerifier {

    private VerificationFactory verificationFactory;

    public ContractVerifier(VerificationFactory verificationFactory) {
        this.verificationFactory = verificationFactory;
    }

    public boolean verify(String baseUrl, Imposter imposter) {
        List<Imposter> imposters = new ArrayList<>();
        imposters.add(imposter);
        return verify(baseUrl, imposters);
    }

    public boolean verify(String baseUrl, List<Imposter> imposters) {
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

        HTTPClient httpClient = verificationFactory.createHttpClient();

        for (Imposter imposter : imposters) {

            HttpPredicate predicate = imposters.get(0).getStub(0).getPredicate(0).getEquals();
            ProviderHTTPResult providerResponse = httpClient.sendRequest(baseUrl, predicate);

            ResponseFields responseFields = imposters.get(0).getStub(0).getResponse(0).getFields();
            MessageHeaderVerifier headerVerifier = verificationFactory.createHeaderVerifier();
            HttpHeaderVerificationResult headerVerificationResult = headerVerifier.verify(responseFields, providerResponse);

            MessageBodyVerifier bodyVerifier = verificationFactory.createBodyVerifier(responseFields, providerResponse);
            BodyVerificationResult bodyVerificationResult = bodyVerifier.verify(responseFields, providerResponse);

            System.out.println(imposters.get(0).getProtocol());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getBody());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getHeaders());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getMethod());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getPath());
            System.out.println(imposters.get(0).getStub(0).getPredicate(0).getEquals().getQueries());


            System.out.println("#######################################");
            verificationFactory.createReport()
                    .useProviderHeaderResult(headerVerificationResult)
                    .useProviderHttpResult(providerResponse)
                    .createConsoleReport().printReport();
            System.out.println("#######################################");

            System.out.println(bodyVerificationResult.getReportStatus());
            System.out.println(headerVerificationResult.getReportStatus());
            System.out.println(providerResponse.getResponseStatus());


        }



        return false;
    }
}
