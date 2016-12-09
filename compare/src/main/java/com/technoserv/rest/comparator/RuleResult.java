package com.technoserv.rest.comparator;

import java.util.ArrayList;

import com.technoserv.rest.model.CompareResponsePhotoObject;

public class RuleResult {

	private boolean fired; // if true, rule is fired
	private String ruleId;
	private String ruleDescription;
	ArrayList<CompareResponsePhotoObject> photos;
	
	
	public RuleResult()
	{
		
	}
	
	public void setPhotos(ArrayList<CompareResponsePhotoObject> photos)
	{
		this.photos = photos;
	}
	
	public ArrayList<CompareResponsePhotoObject> getPhotos()
	{
		return this.photos;
	}
	
	public void addPhoto(CompareResponsePhotoObject photo)
	{
		if (photos != null)
		this.photos.add(photo);
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
}
