package com.technoserv.bio.kernel.objectmodel;

public class StopList extends AbstractObject {
	// Стоп лист. Стоп лист состоит из массива сверток. 
	// Стоп лист может быть доступен либо всем контрагентам, либо списку контрагентов, либо определенному контрагенту.
	String stopListName; // наименование 
	Contractor[] owner; // список банков, которым доступен этот список
	boolean isCommon; // true - список доступен всем контракторам
}
