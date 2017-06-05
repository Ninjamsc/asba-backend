package com.technoserv.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Массив template содержит построенный сервисом биометрический шаблон
 * <p>
 * Created by VBasakov on 22.11.2016.
 */
public class PhotoTemplate {

    /**
     * версия сети, с помощь. которой построен шаблон
     */
    public int version;
    /**
     * массив Numeric	биометрический шаблон
     */
    @JsonProperty("vector")
    public double[] template;
    /**
     * массив байтов биометрического шаблона
     */
    public byte[] binTemplate;

    /**
     * Тип свёртки
     */
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

//    @Override
//    public String toString() {
//        Double[] arr = new Double[template.length];
//        for (int i = 0; i < template.length; i++) {
//            arr[i] = new Double(template[i]);
//        }
//        return new ArrayList<Double>(Arrays.asList(arr)).toString() + "; version = " + version + "; type = " + type +
//                "; bin.vector.length = " + binTemplate.length;
//    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("version", version)
                .add("template length", template.length)
                .add("binTemplate length", binTemplate.length)
                .add("type", type)
                .toString();
    }
}
