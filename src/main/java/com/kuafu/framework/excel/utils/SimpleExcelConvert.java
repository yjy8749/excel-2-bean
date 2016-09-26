package com.kuafu.framework.excel.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangjiayong on 2016/9/23.
 */
public class SimpleExcelConvert implements ExcelConvertInterface{

    @Override
    public String convertTo(Object value, String fieldName,String fieldDesc) {
        String strValue;
        if(value==null){
            strValue = "";
        } else if (value instanceof Integer) {
            strValue = String.valueOf((Integer)value);
        } else if (value instanceof Boolean) {
            strValue = ((Boolean)value)?"是":"否";
        } else if (value instanceof Long) {
            strValue = String.valueOf((Long)value);
        } else if (value instanceof Float) {
            strValue = String.valueOf((Float)value);
        } else if (value instanceof Double) {
            strValue = String.valueOf((Double)value);
        } else if (value instanceof BigDecimal) {
            strValue = ((BigDecimal)value).toPlainString();
        }else if (value instanceof Date) {
            strValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
        } else {
            strValue = value.toString();
        }
        return strValue;
    }

    @Override
    public Object convertFrom(Class type, String value, String fieldName,String fieldDesc,boolean required) throws ExcelFieldConvertException {
        if (StringUtils.isEmpty(value)){
            if(required){
                throw new ExcelFieldConvertException(fieldDesc,value,"不能为空");
            }
            return null;
        }
        if (value.startsWith("=\"")) {
            value = value.substring(2, value.length() - 1);
        }
        if (type == String.class){
            return value;
        } else if (type == Integer.class) {
            try {
                return Integer.valueOf(value);
            }catch (Throwable ex){
                throw new ExcelFieldConvertException(fieldDesc,value,"不是正确的数字类型");
            }
        } else if (type == Boolean.class) {
            return "是".equals(value)||"true".equalsIgnoreCase(value);
        } else if (type == Long.class) {
            try {
                return Long.valueOf(value);
            }catch (Throwable ex){
                throw new ExcelFieldConvertException(fieldDesc,value,"不是正确的数字类型");
            }
        } else if (type == Float.class) {
            try {
                return Float.valueOf(value);
            }catch (Throwable ex){
                throw new ExcelFieldConvertException(fieldDesc,value,"不是正确的数字类型");
            }
        } else if (type == Double.class) {
            try {
                return Double.valueOf(value);
            }catch (Throwable ex){
                throw new ExcelFieldConvertException(fieldDesc,value,"不是正确的数字类型");
            }
        } else if (type == BigDecimal.class) {
            try {
                return new BigDecimal(value);
            }catch (Throwable ex){
                throw new ExcelFieldConvertException(fieldDesc,value,"不是正确的数字类型");
            }
        } else if (type == Date.class) {
            try {
                if(value.length()>10){
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
                }else{
                    return new SimpleDateFormat("yyyy-MM-dd").parse(value);
                }
            } catch (ParseException e) {
                throw new ExcelFieldConvertException(fieldDesc,value,"日期格式错误");
            }
        } else if(type.isEnum()){

            for(Object enumValue : type.getEnumConstants()){
                if(enumValue.toString().equals(value)){
                    return enumValue;
                }
            }

            throw new ExcelFieldConvertException(fieldDesc,value,"枚举未定义");
        }

        throw new ExcelFieldConvertException(fieldDesc,value,"格式转换未定义");

    }
}
