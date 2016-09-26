package com.kuafu.framework.excel.utils;

/**
 * Created by yangjiayong on 2016/9/23.
 */
public class ExcelFieldConvertException extends Exception {

    private String fieldName;

    private String fieldValue;

    private String message;

    public ExcelFieldConvertException(String fieldName, String fieldValue, String message) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }

    @Override
    public String toString() {
        return "ExcelFieldConvertException{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
