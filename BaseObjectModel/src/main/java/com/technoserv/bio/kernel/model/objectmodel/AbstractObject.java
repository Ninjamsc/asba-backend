package com.technoserv.bio.kernel.model.objectmodel;

import java.util.Date;

public abstract class AbstractObject {
	/**
	 *  уникальный идентификатор объекта
	 */
	private Long id;
	/**
	 *  дата создания объекта
	 */
	private Date objectDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getObjectDate() {
		return objectDate;
	}

	public void setObjectDate(Date objectDate) {
		this.objectDate = objectDate;
	}
}