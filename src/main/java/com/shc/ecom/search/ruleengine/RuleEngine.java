package com.shc.ecom.search.ruleengine;

public abstract class RuleEngine {

    IRuleEngineComponent ruleAction;

    public IRuleEngineComponent getRuleAction() {
        return ruleAction;
    }

    public void setRuleAction(IRuleEngineComponent ruleAction) {
        this.ruleAction = ruleAction;
    }

    public void begin(Object obj) {
        ruleAction.exec(obj);
    }
}
