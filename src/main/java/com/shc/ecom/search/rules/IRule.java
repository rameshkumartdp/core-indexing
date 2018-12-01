package com.shc.ecom.search.rules;

import com.shc.ecom.search.common.messages.ContextMessage;

import java.io.Serializable;

/**
 * The rules under this type are for intra-component validations.
 *
 * @param <DomainType>
 * @author rgopala
 */
public interface IRule<DomainType> extends Serializable {

    /**
     * Evaluates if the param confines to this rule.
     *
     * @param domainObj
     * @return true/false - a decision on the evaluation whether the rule passed (true) or failed (false).
     */
    boolean evaluate(DomainType domainObj, ContextMessage context);

}
