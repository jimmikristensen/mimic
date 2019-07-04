package mimic.mountebank;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContractReader {

    public List<String> readContractFilesFromClasspath() throws FileNotFoundException {
        URL url = getClass().getResource("/contracts");
        String path = url.getPath();
        System.out.println(path);
        String[] fileNames = new File(path).list();

        if (fileNames != null) {
            return Arrays.stream(fileNames).map(s -> path+"/"+s).collect(Collectors.toList());
        }
        throw new FileNotFoundException();
    }

    public List<String> readContractFilesFromDir(String dir) throws FileNotFoundException {
        File fileDir = new File(dir);
        String path = fileDir.getAbsolutePath();
        String[] fileNames = fileDir.list();

        if (fileNames != null) {
            return Arrays.stream(fileNames).map(s -> path + "/" + s).collect(Collectors.toList());
        }
        throw new FileNotFoundException();
    }

}
