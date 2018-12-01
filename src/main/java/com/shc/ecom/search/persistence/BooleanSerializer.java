package com.shc.ecom.search.persistence;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by jsingar on 1/24/18.
 */
public class BooleanSerializer  extends JsonSerializer<Boolean> {

    /**
     *
     * This is a hack on the Jackson databind by creating a custom serializer for mapping the primitive data type to String so as to support the groupby datapush API
     */
    @Override
    public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
               jgen.writeString(value.toString());
    }
}