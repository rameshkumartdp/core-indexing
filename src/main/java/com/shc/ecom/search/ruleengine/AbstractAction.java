package com.shc.ecom.search.ruleengine;

public abstract class AbstractAction implements IRuleEngineComponent {

    IRuleEngineComponent nextAction;

    @Override
    public void exec(Object obj) {
        this.doExec(obj);
        if (nextAction != null) {
            nextAction.exec(obj);
        }
    }

    public abstract void doExec(Object obj);

    public IRuleEngineComponent getNextAction() {
        return nextAction;
    }

    public void setNextAction(IRuleEngineComponent nextAction) {
        this.nextAction = nextAction;
    }


}
