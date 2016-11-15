package com.technoserv.bio.kernel.objectmodel;

public enum DocumentType {

	SCAN(1), // со сканера 
	PHOTO(2),  // c веб камеры
	EXTERNAL(3); // то, что банки грузят в стоп листы
	private int code;
	
	 
    private   DocumentType(int code) {
        this.code = code;
    }
 
    public int getCode() {
        return code;
    }
}
