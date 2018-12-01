/**
 *
 */
package com.shc.ecom.search.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author rgopala
 */
@Component
public class SearchFileWriter implements Serializable {

    private static final long serialVersionUID = -285438342878888076L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFileWriter.class.getName());
    public static final String FILE_TYPE = ".json";

    public void writer(List<String> dataList, String filePath, String fileName, String suffix,boolean isEachLine) {
        try {
            Files.createDirectories(Paths.get(filePath));
        } catch (IOException exception) {
            LOGGER.error("Unable to create directory path: " + filePath);
            return;
        }
        String fullyQualifiedName = filePath + fileName + "-" + suffix + FILE_TYPE;
        Path path = Paths.get(fullyQualifiedName);
        LOGGER.info("Writing to file: " + fullyQualifiedName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            if(isEachLine){
                for(String str : dataList) {
                        writer.write(str);
                        writer.newLine();
                 }
            }
            else
            writer.write(dataList.toString());
                   } catch (IOException i) {
            LOGGER.error("IO Exception while writing to file " + fullyQualifiedName);
        }
    }

}
