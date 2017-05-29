package com.technoserv.rest.model;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ruleId", ruleId)
                .add("ruleName", ruleName)
                .add("photo", photo)
                .toString();
    }
}
