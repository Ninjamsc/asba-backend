package com.technoserv.rest.comparator;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.linear.ArrayRealVector;

public class Compare2Photos implements CompareRule {
	
	private String ruleId = "4.2.5";
	private String description = "«Возможно несоответствие фотографии в паспорте и фотографии, прикрепленной к заявке";

	@Override
	public RuleResult doRule(double[] a1, double[] a2) {

				System.out.println("doRule compare 2 vectors");
				RuleResult ret = new RuleResult();
				ret.setRuleId(this.ruleId);
				ret.setRuleDescription(this.description);
				double mult = 0.7f;
				int power = 4;
				double threshold = 0.85;
				ArrayRealVector v1 = new ArrayRealVector(a1);
				ArrayRealVector v2 = new ArrayRealVector(a2);
		   		ArrayRealVector diff =v1.subtract(v2);
				double dot = diff.dotProduct(diff);
				double norm = 1 / new Exp().value(new Pow().value(mult*dot, power));
				if (norm < threshold) ret.setFired(true);
				else ret.setFired(false);
		return null;
	}

	@Override
	public String getRuleId() {
		return this.ruleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
