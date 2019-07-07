package mimic.provider

import mimic.mountebank.exception.ImposterNotFoundException
import mimic.mountebank.provider.ContractFileReader
import mimic.mountebank.net.http.HttpMethod
import spock.lang.Specification
import spock.lang.Unroll


class ContractReaderSpec extends Specification {

    @Unroll
    def "retrieving contract file paths on classpath results in a string list of paths: #filePath"() {
        given:
        def listOfFilePaths = new ContractFileReader().getContractFilesFromClasspath()

        expect:
        listOfFilePaths.size() == 1
        listOfFilePaths.get(id).getPath().endsWith(filePath) == true

        where:
        id  || filePath
        0   || '/contracts/imposter.json'
    }

    @Unroll
    def "retrieving contract file paths on custom location results in a string list of paths: #filePath"() {
        given:
        def path = new File('src/test/resources/custom_contract_dir').getPath()
        def listOfFilePaths = new ContractFileReader().getContractFilesFromDir(path)

        expect:
        listOfFilePaths.size() == 1
        listOfFilePaths.get(id).getPath().endsWith(filePath) == true

        where:
        id  || filePath
        0   || '/custom_contract_dir/imposter_in_custom_dir.json'
    }

    def "providing invalid dir to custom contract locater results in FileNotFoundException"() {
        when:
        def path = new File('dir_that_does_not_exist').getPath()
        def listOfFilePaths = new ContractFileReader().getContractFilesFromDir(path)

        then:
        ImposterNotFoundException ex = thrown()
        ex.message.startsWith('Did not find any local imposter contract files at path:') == true
    }

    def "searching for imposters in a non-empty dir with no imposters results in FileNotFoundException"() {
        when:
        def imposters = new ContractFileReader('src/test/resources/not_a_contract_dir').readContract()

        then:
        ImposterNotFoundException ex = thrown()
        ex.message.startsWith('No imposter contract files found') == true
    }

    @Unroll
    def "attempting to read contract file from path returns an Imposter object"() {
        given:
        def imposters = new ContractFileReader('src/test/resources/custom_contract_dir').readContract()

        expect:
        imposters.size() == 1
        imposters.get(id).getStub(0).getPredicate(0).getEquals().getMethod() == method
        imposters.get(id).getStub(0).getResponse(0).getFields().getStatus() == status

        where:
        id  || method           | status
        0   || HttpMethod.GET   | 201
    }

    @Unroll
    def "attempting to read contract file from classpath returns an Imposter object"() {
        given:
        def imposters = new ContractFileReader().readContract()

        expect:
        imposters.size() == 1
        imposters.get(id).getStub(0).getPredicate(0).getEquals().getMethod() == method
        imposters.get(id).getStub(0).getResponse(0).getFields().getStatus() == status

        where:
        id  || method           | status
        0   || HttpMethod.GET   | 201
    }
}