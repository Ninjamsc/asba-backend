package com.technoserv.rest.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    
    @Override
    public String toString()
    {
    	Double[] arr =  new Double[template.length];
    	for(int i=0;i< template.length;i++)
    		arr[i] = new Double(template[i]);
    	return arr.toString();
    }

}
