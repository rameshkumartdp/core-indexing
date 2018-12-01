/**
 *
 */
package com.shc.ecom.search.persistence;

import com.google.gson.JsonSyntaxException;
import com.shc.ecom.search.common.constants.DataFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author rgopala
 */
@Component
public class SearchFileReader implements Serializable {

    private static final long serialVersionUID = -8576894624489706466L;

    @Autowired
    private PersistenceFactory persistenceFactory;

    /**
     * Given a fileName this method loads the data into its appropriate data pojo.
     * It does not distinguish between reading from a file or a service, it is handled by the appropriate DataAccessor
     *
     * @param fileName
     * @throws IOException
     */
    public void readResource(String fileName) throws IOException {
        // Get a handle on the data object
        DataAccessor file = persistenceFactory.obtain(fileName);
        if (file == null) { // Necessary ?
                return;
            }
        // Read the appropriate file or service
        file.read(fileName);
    }
}
