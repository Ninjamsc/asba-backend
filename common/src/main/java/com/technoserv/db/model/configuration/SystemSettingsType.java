package com.technoserv.db.model.configuration;

/**
 * Created by 90630 on 14.12.2016.
 */
public enum SystemSettingsType {
    PHOTO_PERSIST_SERVICE_URL("http://localhost:8080/storage/rest/image"),
    COMPARE_SERVICE_URL("http://localhost:8080/compare/compare"),
    PHOTO_ANALYZER_SERVICE_URL("http://localhost:8080/analize/analize"),
    TEMPLATE_BUILDER_SERVICE_URL("http://localhost:8080/template/template");

    private final String defauldValue;

    SystemSettingsType(String defauldValue) {
        this.defauldValue = defauldValue;
    }

    public String getDefauldValue() {
        return defauldValue;
    }
}
