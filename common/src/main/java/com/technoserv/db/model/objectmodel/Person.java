package com.technoserv.db.model.objectmodel;

import javax.persistence.*;
import java.util.List;

/**
 *Хранилище связок ИИН,
 * который идентифицирует заявителя
 * с полученными во время оформления заявки биометрическими материалами (фото)
 */
@Entity
@Table(name = "PERSONS")
public class Person extends AbstractObject {
	/**
	 * массив всех заявок
	 */
	@OneToMany(mappedBy = "person")
	private List<Request> dossier;
	/**
	 * ИИН персоны в WorkFlow
	 */
	@Column(name = "IIN")
	private String iin;

	public List<Request> getDossier() {
		return dossier;
	}
	public void setDossier(List<Request> dossier) {
		this.dossier = dossier;
	}

	public String getIin() {
		return iin;
	}
	public void setIin(String iin) {
		this.iin = iin;
	}
}