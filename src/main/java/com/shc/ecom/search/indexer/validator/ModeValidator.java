package com.shc.ecom.search.indexer.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.shc.ecom.search.common.messages.MessageType;

/**
 * Created by tgulati on 8/30/16.
 */
public class ModeValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {

        if (MessageType.isMessageTypeValid(value)) {
            return;
        } else {
            throw new ParameterException("Incorrect store passed: " + value);
        }


    }

}
