package com.technoserv.bio.kernel.model.configuration;

import com.technoserv.bio.kernel.model.objectmodel.AbstractObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FrontEndConfiguration extends AbstractObject {

	@Id
	private long temporaryID;

	public long getTemporaryID() {
		return temporaryID;
	}

	public void setTemporaryID(long temporaryID) {
		this.temporaryID = temporaryID;
	}

	@Column
	private Integer version; // TODO: string val
	// key value

	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
