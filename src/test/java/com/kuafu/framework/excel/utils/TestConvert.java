package com.kuafu.framework.excel.utils;

/**
 * Created by yangjiayong on 2016/9/26.
 */
public class TestConvert extends SimpleExcelConvert{

    @Override
    public String convertTo(Object value, String fieldName, String fieldDesc) {
        System.out.println("Test Customer convertTo");
        return super.convertTo(value, fieldName, fieldDesc);
    }

    @Override
    public Object convertFrom(Class type, String value, String fieldName, String fieldDesc, boolean required) throws ExcelFieldConvertException {
        System.out.println("Test Customer convertFrom");
        return super.convertFrom(type, value, fieldName, fieldDesc, required);
    }

}
