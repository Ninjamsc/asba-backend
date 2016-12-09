package com.technoserv.rest.model;



import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CompareTestResponse {

private Double[] vector;// = {1d,2d,3d,4d};

public Double[] getVector() {
	return vector;
}

//public void setVector(Double[] vector) {
//	this.vector = vector;
//}

public void setVector(double[] a) {
	vector = new Double[a.length];
	for(int i=0; i<a.length;i++)
	this.vector[i] = new Double( a[i]);
}


}
