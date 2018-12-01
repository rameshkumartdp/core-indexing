package com.shc.ecom.search.persistence;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 * Created by hdargah on 11/2/2016.
 */
public interface DataAccessor {
    void read(String fileName) throws IOException, JsonSyntaxException;

}
