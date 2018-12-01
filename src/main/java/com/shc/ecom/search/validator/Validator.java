package com.shc.ecom.search.validator;

import com.shc.common.index.rules.ValidationResults;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageType;
import com.shc.ecom.search.rules.IRule;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author rgopala
 */
@Component
public class Validator implements Serializable {

    private static final long serialVersionUID = -3587058994236855252L;

    /**
     * Validates the subject based on the rules.  Current implementation is fail-fast: the method returns as soon as one of the rules fails.
     *
     * @param subject - the domain object that needs to be validated for conformance.
     * @param rules   - list of rules of type IRule, with same type arg as that of subject. Each rule is evaluated w.r.t subject.
     * @return ValidationResults - a result that tells if the validation passed. It also provides a data-map of rules that were applied and their status.
     */
    public <T> ValidationResults validate(T subject, List<IRule<T>> rules, ContextMessage context) {
        ValidationResults validationResults = new ValidationResults();
        for (IRule<T> rule : rules) {
            if (!rule.evaluate(subject, context)) {
                //If the indexer is running in dry run mode, aggregate all the rules that failed but do not fail-fast
                if (MessageType.getMessageType(context.getMessageId()) == MessageType.DRYRUN_OFFERID) {

                    validationResults.getDataMap().put(rule.getClass().getSimpleName(), false);
                    continue;
                } else {
                    validationResults.setPassed(false);
                    validationResults.getDataMap().put(rule.getClass().getSimpleName(), false);
                    validationResults.setRejectedRule(rule.getClass().getSimpleName());
                    return validationResults;
                }
            }
            validationResults.getDataMap().put(rule.getClass().getSimpleName(), true);
        }
        validationResults.setPassed(true);
        return validationResults;
    }
}
