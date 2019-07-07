package mimic.provider

import mimic.mountebank.provider.verifier.ContractVerification
import mimic.mountebank.provider.verifier.ContractVerifier
import spock.lang.Specification


class ContractVerifierSpec extends Specification {

    def "verifying contract with matchin statuc code succeeds"() {
        given:
        def contractVerifier = new ContractVerifier(new ContractVerification())

        when:
        contractVerifier
    }

}