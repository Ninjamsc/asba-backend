package com.technoserv.db.model.configuration;

/**
 * Created by 90630 on 14.12.2016.
 */
public enum SystemSettingsType {
    PHOTO_PERSIST_SERVICE_URL("http://www.sdorohov.ru/storage/api/rest/image"),
    COMPARE_SERVICE_URL("http://localhost:8080/rpe/api/rest/compare-stub/template"),
    PHOTO_ANALYZER_SERVICE_URL("http://localhost:8080/analize/analize"),
    TEMPLATE_BUILDER_SERVICE_URL("http://localhost:8080/rpe/api/rest/template-builder-stub"),
    COMPARATOR_COMMON_LIST_ID("5"),
    COMPARATOR_MULTIPLIER("0.7"),
    COMPARATOR_POWER("4"),
    RULE_SELF_SIMILARITY("0.854321");

    private final String defauldValue;

    SystemSettingsType(String defauldValue) {
        this.defauldValue = defauldValue;
    }

    public String getDefauldValue() {
        return defauldValue;
    }
}
