package com.technoserv.bio.kernel.rest.response;

/**
 * Массив template содержит построенный сервисом биометрический шаблон
 *
 * Created by VBasakov on 22.11.2016.
 */
public  class TemplateBuilderResponse {
    /** версия сети, с помощь. которой построен шаблон */
    public int version;
    /** массив Numeric	биометрический шаблон */
    public double[] template;
}
