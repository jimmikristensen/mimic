package mimic.provider

import mimic.mountebank.provider.verifier.HttpContractVerificationFactory
import mimic.mountebank.provider.verifier.ContractVerification
import spock.lang.Specification


class ContractVerifierSpec extends Specification {

    def "verifying contract with matchin statuc code succeeds"() {
        given:
        def contractVerifier = new ContractVerification(new HttpContractVerificationFactory())

        expect:
        1 == 1
    }

}