package com.technoserv.bio.kernel.model.objectmodel;

public class Convolution extends AbstractObject {
	byte[]		convolution; // сам бинарный вектор свертки
	int			cnnVersion; // версия нейронной сети, с помощью которой свертка получена
	StopList[]	stopListEntries; // список ссылок на Стоп листов, в которые входит эта свертка

}
