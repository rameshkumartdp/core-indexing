/**
 *
 */
package com.shc.ecom.search.docbuilder;

import com.shc.ecom.search.common.messages.ContextMessage;

/**
 * @author rgopala
 */
public interface Builder<T> {

    T build(ContextMessage context);
}
