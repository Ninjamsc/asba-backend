package com.technoserv.db.model.objectmodel;

import javax.persistence.Column;

/**
 * Справочник типов биометрических шаблонов (Фото, вены, отпечатки пальцев и т.п.)
 */
public class BioTemplateType extends AbstractObject {

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
