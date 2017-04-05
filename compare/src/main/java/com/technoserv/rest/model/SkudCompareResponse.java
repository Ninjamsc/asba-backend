
package com.technoserv.rest.model;
		import java.util.ArrayList;
		import com.fasterxml.jackson.annotation.JsonInclude;
		import com.fasterxml.jackson.annotation.JsonInclude.Include;
		import com.technoserv.db.model.objectmodel.Document;

public class SkudCompareResponse  {


	@JsonInclude(Include.NON_EMPTY)
	private CompareResponsePhotoObject match;

	public CompareResponsePhotoObject getMatch() {
		return match;
	}

	public void setMatch(CompareResponsePhotoObject match) {
		this.match = match;
	}

	@Override
	public String toString() {
		return "CompareRequest  matches="+match+"]";
	}
}
