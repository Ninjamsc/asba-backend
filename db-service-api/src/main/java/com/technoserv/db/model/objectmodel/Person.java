package com.technoserv.db.model.objectmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 *-
 */
@Entity
@Table(name = "PERSONS")
public class Person extends AbstractObject {
	/**
	 * массив всех заявок
	 */
	@OneToMany
	private List<Request> dossier;
	/**
	 * ИИН персоны в WorkFlow
	 */
	@Column(name = "PERSON_NUMBER")
	private String personNumber;

	public List<Request> getDossier() {
		return dossier;
	}
	public void setDossier(List<Request> dossier) {
		this.dossier = dossier;
	}

	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
}