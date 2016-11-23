package com.technoserv.db.model.objectmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Справочник версий биометрических шаблонов. Шаблон имеет версию.
 * На базе данного документа может быть построено несколько версий биометрического шаблона данного типа
 */
@Entity
@Table(name="BIO_TEMPLATES_VERSION")
public class BioTemplateVersion extends AbstractObject {

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
