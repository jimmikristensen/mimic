package mimic.mountebank.producer;

import mimic.mountebank.exception.ImposterNotFoundException;
import mimic.mountebank.imposter.Imposter;
import mimic.mountebank.net.databind.JacksonObjectMapper;
import mimic.mountebank.exception.ImposterParseException;

import java.io.File;
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

    private List<File> getContractFilesFromClasspath() {
        URL url = getClass().getResource("/contracts");
        String path = url.getPath();
        String[] fileNames = new File(path).list();

        if (fileNames != null) {
            return getFiles(path, fileNames);
        }

        throw new ImposterNotFoundException("Did not find any local imposter contract files at path: " + path);
    }

    private List<File> getContractFilesFromDir(String dir) {
        File fileDir = new File(dir);
        String path = fileDir.getAbsolutePath();
        String[] fileNames = fileDir.list();

        if (fileNames != null) {
            return getFiles(path, fileNames);
        }

        throw new ImposterNotFoundException("Did not find any local imposter contract files at path: " + path);
    }

    private List<File> getFiles(String path, String[] fileNames) {
        return Arrays.stream(fileNames)
                .filter(s -> s.endsWith(".json") || s.endsWith(".ejs"))
                .map(s -> new File(path + "/" + s))
                .collect(Collectors.toList());
    }


    public List<Imposter> readContract() {
        List<File> contractFiles;

        if (contractFilePath != null) {
            contractFiles = getContractFilesFromDir(contractFilePath);
        } else {
            contractFiles = getContractFilesFromClasspath();
        }

        if (contractFiles != null && !contractFiles.isEmpty()) {
            List<Imposter> imposters = contractFiles.stream().map(s -> {

                try {
                    return JacksonObjectMapper.getMapper().readValue(s, Imposter.class);
                } catch (IOException e) {
                    throw new ImposterParseException("Unable to parse contract imposter to object", e);
                }

            }).collect(Collectors.toList());

            return imposters;
        }

        throw new ImposterNotFoundException("No imposter contract files found");
    }
}
