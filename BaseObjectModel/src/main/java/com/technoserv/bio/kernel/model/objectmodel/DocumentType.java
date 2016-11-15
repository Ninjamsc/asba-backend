package com.technoserv.bio.kernel.model.objectmodel;

import com.technoserv.bio.kernel.model.configuration.FrontEndType;

public enum DocumentType {

    SCAN(1), // со сканера
    PHOTO(2),  // c веб камеры
    EXTERNAL(3); // то, что банки грузят в стоп листы
    private int code;

    private DocumentType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static DocumentType parse(int id) {
        DocumentType type = null; // Default
        for (DocumentType item : DocumentType.values()) {
            if (item.getCode() == id) {
                type = item;
                break;
            }
        }
        return type;
    }
}