package com.technoserv.db.model.objectmodel;

import javax.persistence.*;

/**
 * Created by Adrey on 14.12.2016.
 */
@Entity
@Table(name = "COMPARE_RESULTS")
public class CompareResult {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column
    @Lob
    private String json;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }
    public void setJson(String json) {
        this.json = json;
    }
}