package mimic

import mimic.mountebank.ContractReader
import spock.lang.Specification
import spock.lang.Unroll


class ContractReaderSpec extends Specification {

    @Unroll
    def "retrieving contract file paths on classpath results in a string list of paths: #filePath"() {
        given:
        def listOfFilePaths = new ContractReader().readContractFilesFromClasspath()

        expect:
        listOfFilePaths.size() == 1
        listOfFilePaths.get(id).endsWith(filePath) == true

        where:
        id  || filePath
        0   || '/contracts/imposter.json'
    }

}