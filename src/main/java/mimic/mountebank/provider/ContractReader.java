package mimic.mountebank.provider;

import mimic.mountebank.imposter.Imposter;

import java.util.List;

public interface ContractReader {

    public List<Imposter> readContract();
}
