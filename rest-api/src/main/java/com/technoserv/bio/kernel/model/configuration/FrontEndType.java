package com.technoserv.bio.kernel.model.configuration;

public enum FrontEndType {

	OPERATOR(1), ADMIN(2);
	private int code;

    private   FrontEndType(int code) {
        this.code = code;
    }
 
    public int getCode() {
        return code;
    }

    public static FrontEndType parse(int id) {
        FrontEndType type = null; // Default
        for (FrontEndType item : FrontEndType.values()) {
            if (item.getCode() == id) {
                type = item;
                break;
            }
        }
        return type;
    }
}