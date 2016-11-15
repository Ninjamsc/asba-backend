package com.technoserv.bio.kernel.configuration;

public enum FrontEndType {

	OPERATOR(1), ADMIN(2);
	private int code;
	
	 
    private   FrontEndType(int code) {
        this.code = code;
    }
 
    public int getCode() {
        return code;
    }
}
