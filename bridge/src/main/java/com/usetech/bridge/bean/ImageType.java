package com.usetech.bridge.bean;

public enum ImageType {

    PREVIEW("preview"), FULLFRAME("fullframe");

    private String imageType;

    ImageType(String fullframe) {
        this.imageType = fullframe;
    }

    public static ImageType findByCode(String imageCode) {
        for (ImageType type : ImageType.values()) {
            if (!type.imageType.equalsIgnoreCase(imageCode))
                continue;
            return type;
        }
        return null;
    }

    public String getImageType() {
        return this.imageType;
    }
}