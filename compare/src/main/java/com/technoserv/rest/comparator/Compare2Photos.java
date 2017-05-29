package com.technoserv.rest.comparator;

import com.technoserv.utils.TevianVectorComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

public class Compare2Photos implements CompareRule {

    private static final Logger log = LoggerFactory.getLogger(Compare2Photos.class);

    private String ruleId = "4.2.5";
    private String description = "«Возможно несоответствие фотографии в паспорте и фотографии, прикрепленной к заявке";

    @Override
    public RuleResult doRule(double[] a1, double[] a2) {
        log.debug("doRule (compare two vectors) a1: {} a2: {}", a1, a2);

        RuleResult ret = new RuleResult();
        ret.setRuleId(this.ruleId);
        ret.setRuleDescription(this.description);

        double mult = 0.7f;
        int power = 4;
        double threshold = 0.85;
        /*
                ArrayRealVector v1 = new ArrayRealVector(a1);
				ArrayRealVector v2 = new ArrayRealVector(a2);
		   		ArrayRealVector diff =v1.subtract(v2);
				double dot = diff.dotProduct(diff);
				double norm = 1 / new Exp().value(new Pow().value(mult*dot, power));
		*/
//		double norm = TevianVectorComparator.calculateSimilarityWrapper(a1,a2);

        double norm = TevianVectorComparator.calculateSimilarityCliWrapper(
                new String(Base64.encode(TevianVectorComparator.getByteArrayFromVector(a1))),
                new String(Base64.encode(TevianVectorComparator.getByteArrayFromVector(a1))), "1");

        ret.setFired(norm < threshold);
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
