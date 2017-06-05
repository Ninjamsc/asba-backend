package com.technoserv.rest.model;

import java.util.List;

public class CompareResponseRulesObject {

    private String ruleId;

    private String ruleName;

    private List<CompareResponsePhotoObject> photo;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public List<CompareResponsePhotoObject> getPhoto() {
        return photo;
    }

    public void setPhoto(List<CompareResponsePhotoObject> photo) {
        this.photo = photo;
    }

}
