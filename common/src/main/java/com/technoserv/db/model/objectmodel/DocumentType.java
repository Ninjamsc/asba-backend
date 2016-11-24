package com.technoserv.db.model.objectmodel;


import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;

/**
 * Справочник типов биометрических шаблонов (Фото, вены, отпечатки пальцев и т.п.)
 */
@Entity
@Table(name = "DOCUMENTS_TYPES")
@Cacheable
public class DocumentType extends BaseEntity<Long> {


    /**
     * 0 - ANY
     * 1 - SCANNER
     * 2 - WEBCAM
     * 3 - STOPLIST
     */
    public enum Type{ANY(0), SCANNER(1), WEB_CAM(2), STOP_LIST(3);
        private long value;

        Type(long value) {
            this.value = value;
        }
        public long getValue() {
            return value;
        }
    };

    public DocumentType() { }
    public DocumentType(Type type) {
        this.id = type.getValue();
    }
    public DocumentType(Type type, String description) {
        this.type = type;
        this.description = description;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "TYPE",unique = true)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}