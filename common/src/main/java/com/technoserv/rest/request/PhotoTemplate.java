package com.technoserv.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Массив template содержит построенный сервисом биометрический шаблон
 *
 * Created by VBasakov on 22.11.2016.
 */
public  class PhotoTemplate {

    /** версия сети, с помощь. которой построен шаблон */
    public int version;
    /** массив Numeric	биометрический шаблон */
    @JsonProperty("vector")
    public double[] template;

    /**Тип свёртки*/
    public int type;

    public void setVersion(int version) {
        this.version = version;
    }

    public void setTemplate(double[] template) {
        this.template = template;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString()
    {
    	Double[] arr =  new Double[template.length];
    	for(int i=0;i< template.length;i++)
    		arr[i] = new Double(template[i]);
    	return new ArrayList<Double>(Arrays.asList(arr)).toString() + "; version = " + version + "; type = " + type;
    }

}
