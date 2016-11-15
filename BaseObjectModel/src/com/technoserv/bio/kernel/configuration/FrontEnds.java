package com.technoserv.bio.kernel.configuration;

import com.technoserv.bio.kernel.objectmodel.AbstractObject;

public class FrontEnds extends AbstractObject {

	FrontEndType feType;
	Integer version; // TODO:нужно major minor
	//TODO: нужно сделать массив объетов класса. у каждого объекта ссылка на установенную ему конфигурацию и версию АРМ
}
