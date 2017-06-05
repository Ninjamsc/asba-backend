package com.technoserv.rest.comparator;

public interface CompareRule {

    RuleResult doRule(double[] v1, double[] v2);

    String getRuleId();

    String getDescription();
}
