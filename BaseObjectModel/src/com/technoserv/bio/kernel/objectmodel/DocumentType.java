package com.technoserv.bio.kernel.objectmodel;

public enum DocumentType {

	SCAN(1), // �� ������� 
	PHOTO(2),  // c ��� ������
	EXTERNAL(3); // ��, ��� ����� ������ � ���� �����
	private int code;
	
	 
    private   DocumentType(int code) {
        this.code = code;
    }
 
    public int getCode() {
        return code;
    }
}
