package com.technoserv.db.model.configuration;

import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;

/**
 * Created by 90630 on 14.12.2016.
 */
@Entity
@Table(name = "SYSTEM_SETTINGS")
public class SystemSettings extends BaseEntity<SystemSettingsType> {

    @Id
    @Enumerated(EnumType.STRING)
    private SystemSettingsType id;

    @Column
    private String value;

    @Override
    public SystemSettingsType getId() {
        return id;
    }

    public void setId(SystemSettingsType id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
