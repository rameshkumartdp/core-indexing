package com.shc.ecom.search.extract.components;

import com.shc.common.index.rules.Decision;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.extracts.WorkingDocument;

/**
 * @param <T>
 * @author pchauha
 */
public interface Validatable<T> {

    Decision validate(T source, WorkingDocument wd, ContextMessage context);
}
