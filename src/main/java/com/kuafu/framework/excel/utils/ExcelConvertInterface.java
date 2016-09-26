package com.kuafu.framework.excel.utils;

/**
 * Created by yangjiayong on 2016/9/23.
 */
public interface ExcelConvertInterface {

    public String convertTo(Object value,String fieldName,String fieldDesc);

    public Object convertFrom(Class type,String value,String fieldName,String fieldDesc,boolean required) throws ExcelFieldConvertException;

}
