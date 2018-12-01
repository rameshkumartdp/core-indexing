package com.shc.ecom.search.indexer.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by tgulati on 8/31/16.
 */
public class ServerValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {

        if (Integer.valueOf(value) < 0) {
            throw new ParameterException("Server should be non zero");
        }

    }

}
