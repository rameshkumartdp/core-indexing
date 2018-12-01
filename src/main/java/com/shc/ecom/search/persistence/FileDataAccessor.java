package com.shc.ecom.search.persistence;

import com.google.gson.JsonSyntaxException;
import com.shc.ecom.search.common.constants.DataFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Reads resource from a file and implements the methods of DataAccessor.
 * Extend this class when want to read from file line-by-line. This is the existing way of reading files
 * If you have an alternate implementation then override read(String filname)
 * <p>
 * Created by hdargah on 11/2/2016.
 */
public abstract class FileDataAccessor implements DataAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDataAccessor.class.getName());
    private static final String EOF = "EOF";
    private static String errorMessage = "Could not find ";

    @Override
    public void read(String fileName) throws IOException, JsonSyntaxException {
        LOGGER.info("Reading file: " + fileName);
        Class<?> clazz;
        try {
            clazz = Class.forName(this.getClass().getSimpleName());
        } catch (ClassNotFoundException e) {
            LOGGER.error("Exception while getting name: " + e);
            clazz = this.getClass();
        }
        // returns the ClassLoader object associated with this Class
        ClassLoader classLoader = clazz.getClassLoader();

        // An input stream for reading the resource, or null if the resource could not be found
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            if (isNotRequiredFile(fileName)) {
                return;
            }
            throw new IOException("Input resource could not be found: " + fileName);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        // reads each line
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            this.save(line);
        }
        this.save(EOF);
        bufferedReader.close();
        inputStream.close();
    }
    
    /**
     * Return true is file in context is not a required file for indexing.
     * PidMapping, Expirable Field, Service Mapping are not required files
     * @param fileName
     * @return
     */
    private boolean isNotRequiredFile(String fileName){
        if (DataFile.PID_MAPPING.getFilename().equalsIgnoreCase(fileName)) {
            LOGGER.error(errorMessage + fileName + " ,Disabling PidMapping Feature");
            return true;
        }
        if (DataFile.EXPIRABLEFIELDS.getFilename().equalsIgnoreCase(fileName)) {
            LOGGER.error(errorMessage + fileName + " ,Disabling Expirable Field Extraction Feature");
            return true;
        }
        if (DataFile.SERVICEMAPPING.getFilename().equalsIgnoreCase(fileName)) {
            LOGGER.error(errorMessage + fileName + " ,Disabling Service Mapping Feature");
            return true;
        }
        return false;
    }

    abstract void save(String value);
}
