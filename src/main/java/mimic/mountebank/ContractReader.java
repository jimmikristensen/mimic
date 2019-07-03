package mimic.mountebank;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContractReader {

    public List<String> readContractFilesFromClasspath() {
        URL url = getClass().getResource("/contracts");
        String path = url.getPath();
        String[] fileNames = new File(path).list();

        return Arrays.stream(fileNames).map(s -> path+"/"+s).collect(Collectors.toList());
    }

}
