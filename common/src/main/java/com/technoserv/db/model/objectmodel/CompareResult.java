package com.technoserv.db.model.objectmodel;

import com.google.common.base.MoreObjects;
import com.technoserv.db.model.BaseEntity;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

/**
 * Created by Adrey on 14.12.2016.
 */
@Entity
@Table(name = "COMPARE_RESULTS")
public class CompareResult extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "result", length = 20000)
    @Lob
    private String json;

    public CompareResult() {
    }

    public CompareResult(Long id, String json) {
        this.id = id;
        this.json = json;
    }

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("json length", StringUtils.length(json))
                .toString();
    }

}