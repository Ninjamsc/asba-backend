package com.technoserv.rest.model;

import java.util.ArrayList;

public class CompareResponseRulesObject {
    private String ruleId;
    private String ruleName;
    private ArrayList<CompareResponsePhotoObject> photo;

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

    public ArrayList<CompareResponsePhotoObject> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<CompareResponsePhotoObject> photo) {
        this.photo = photo;
    }
}
