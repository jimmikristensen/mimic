package mimic.mountebank.provider;

import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.provider.verifier.ContractVerifier;
import mimic.mountebank.provider.verifier.HttpVerificationFactory;
import mimic.mountebank.provider.verifier.VerificationFactory;

import java.util.List;

public class ProviderImposterVerifier {

    public static Boolean Verify() {
        return true;
    }

    public static ProviderImposterVerifierBuilder Builder(String baseUrl) {
        return new ProviderImposterVerifierBuilder(baseUrl);
    }

    private static class ProviderImposterVerifierBuilder {

        private ContractReader contractReader = new ContractFileReader();
        private List<Imposter> imposters;
        private String baseUrl;
        private VerificationFactory httpVerificationFactory = new HttpVerificationFactory();

        private ProviderImposterVerifierBuilder(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public ProviderImposterVerifierBuilder useContractsFromClasspath() {
            imposters = contractReader.readContract();
            return this;
        }

        public ProviderImposterVerifierBuilder useContractFromFile() {
            return this;
        }

        public ProviderImposterVerifierBuilder useContractsFromMountebank() {
            return this;
        }

        public ProviderImposterVerifierBuilder verify() {
            new ContractVerifier(httpVerificationFactory).verify(baseUrl, imposters);
            return this;
        }

        public void getReport() {

        }
    }
}
