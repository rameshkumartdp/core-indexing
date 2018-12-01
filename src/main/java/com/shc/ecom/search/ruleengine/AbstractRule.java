package com.shc.ecom.search.ruleengine;

public abstract class AbstractRule implements IRuleEngineComponent {

    IRuleEngineComponent positiveOutcomeAction;

    IRuleEngineComponent negativeOutcomeAction;


    @Override
    public void exec(Object obj) {
        if (evaluate(obj)) {
            positiveOutcomeAction.exec(obj);
        } else {
            negativeOutcomeAction.exec(obj);
        }

    }

    protected abstract boolean evaluate(Object obj);


    public IRuleEngineComponent getPositiveOutcomeAction() {
        return positiveOutcomeAction;
    }

    public void setPositiveOutcomeAction(IRuleEngineComponent positiveOutcomeAction) {
        this.positiveOutcomeAction = positiveOutcomeAction;
    }

    public IRuleEngineComponent getNegativeOutcomeAction() {
        return negativeOutcomeAction;
    }

    public void setNegativeOutcomeAction(IRuleEngineComponent negativeOutcomeAction) {
        this.negativeOutcomeAction = negativeOutcomeAction;
    }

}
