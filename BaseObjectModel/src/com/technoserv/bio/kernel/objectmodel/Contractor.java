package com.technoserv.bio.kernel.objectmodel;

public class Contractor extends AbstractObject {

	String contractorName;
	StopList[] contractorLists;
	float similarityThresold; // ���� ��������
	Document[] loadedImages; //����������� ������ ���������� (�� ��������� ���)
}
