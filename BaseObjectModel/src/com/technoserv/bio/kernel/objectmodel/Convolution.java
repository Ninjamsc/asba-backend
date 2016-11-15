package com.technoserv.bio.kernel.objectmodel;

public class Convolution extends AbstractObject {
	byte[]		convolution; // сам бинарный вектор свертки
	int			cnnVersion; // верси€ нейронной сети, с помощью которой свертка получена
	StopList[]	stopListEntries; // список ссылок на —топ листов, в которые входит эта свертка

}
