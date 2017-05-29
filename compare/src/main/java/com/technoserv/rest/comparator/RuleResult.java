package com.technoserv.rest.comparator;

import com.google.common.base.MoreObjects;

public class RuleResult {

    private boolean fired; // if true, rule is fired

    private String ruleId;

    private String ruleDescription;

    public RuleResult() {

    }

    public boolean isFired() {
        return fired;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fired", fired)
                .add("ruleId", ruleId)
                .add("ruleDescription", ruleDescription)
                .toString();
    }
}
