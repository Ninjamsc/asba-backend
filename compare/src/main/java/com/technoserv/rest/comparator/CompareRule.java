package com.technoserv.rest.comparator;

public interface CompareRule {

	public RuleResult doRule(double[]v1, double[] v2);
	public String getRuleId();
	public String getDescription();
}
