package com.technoserv.bio.kernel.objectmodel;

public class StopList extends AbstractObject {
	// ���� ����. ���� ���� ������� �� ������� �������. 
	// ���� ���� ����� ���� �������� ���� ���� ������������, ���� ������ ������������, ���� ������������� �����������.
	String stopListName; // ������������ 
	Contractor[] owner; // ������ ������, ������� �������� ���� ������
	boolean isCommon; // true - ������ �������� ���� ������������
}
