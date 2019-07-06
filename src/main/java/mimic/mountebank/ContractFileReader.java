package mimic.mountebank;

import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.net.databind.JacksonObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContractFileReader implements ContractReader {

    private String contractFilePath;

    public ContractFileReader(String contractFile) {
        this.contractFilePath = contractFile;
    }

    public ContractFileReader() {}

    private List<File> getContractFilesFromClasspath() throws FileNotFoundException {
        URL url = getClass().getResource("/contracts");
        String path = url.getPath();
        String[] fileNames = new File(path).list();

        if (fileNames != null) {
            return getFiles(path, fileNames);
        }
        throw new FileNotFoundException();
    }

    private List<File> getContractFilesFromDir(String dir) throws FileNotFoundException {
        File fileDir = new File(dir);
        String path = fileDir.getAbsolutePath();
        String[] fileNames = fileDir.list();

        if (fileNames != null) {
            return getFiles(path, fileNames);
        }
        throw new FileNotFoundException();
    }

    private List<File> getFiles(String path, String[] fileNames) {
        return Arrays.stream(fileNames)
                .filter(s -> s.endsWith(".json") || s.endsWith(".ejs"))
                .map(s -> new File(path + "/" + s))
                .collect(Collectors.toList());
    }


    public List<Imposter> readContract() {
        List<File> contractFiles = null;

        try {
            if (contractFilePath != null) {
                contractFiles = getContractFilesFromDir(contractFilePath);
            } else {
                contractFiles = getContractFilesFromClasspath();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (contractFiles != null) {
            List<Imposter> imposters = contractFiles.stream().map(s -> {

                try {
                    return JacksonObjectMapper.getMapper().readValue(s, Imposter.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }).collect(Collectors.toList());

            return imposters;
        }

        return null;
    }
}
