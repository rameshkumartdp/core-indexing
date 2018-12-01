package com.shc.ecom.search.indexer.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.shc.ecom.search.common.constants.Stores;

/**
 * Created by tgulati on 8/30/16.
 */
public class StoreValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {

        if (Stores.isStoreValid(value)) {
            return;
        } else {
            throw new ParameterException("Incorrect store passed: " + value);
        }

    }

}
