package mimic.producer

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

    @Unroll
    def "retrieving contract file paths on custom location results in a string list of paths: #filePath"() {
        given:
        def path = new File('src/test/resources/custom_contract_dir').getPath()
        def listOfFilePaths = new ContractReader().readContractFilesFromDir(path)

        expect:
        listOfFilePaths.size() == 1
        listOfFilePaths.get(id).endsWith(filePath) == true

        where:
        id  || filePath
        0   || '/custom_contract_dir/imposter_in_custom_dir.json'
    }
}