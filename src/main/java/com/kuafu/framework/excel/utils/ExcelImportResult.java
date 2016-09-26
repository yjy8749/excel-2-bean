package com.kuafu.framework.excel.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjiayong on 2016/9/23.
 */
public class ExcelImportResult<T> {

    private List<T> results = new ArrayList<T>();

    private List<FieldError> errorList = new ArrayList<FieldError>();

    public ExcelImportResult() {

    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public List<FieldError> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<FieldError> errorList) {
        this.errorList = errorList;
    }

    public boolean isAllSuccess(){
        return this.errorList.isEmpty();
    }

    public boolean isAllFailed(){
        return this.results.isEmpty();
    }

    public boolean isSuccess(){
        return !this.results.isEmpty();
    }

    public boolean isFailed(){
        return !this.errorList.isEmpty();
    }

    public static class FieldError {

        private int lineNumber;

        private String[] lineContent;

        private String fieldName;

        private String fieldValue;

        private String message;

        public FieldError(int lineNumber,String[] lineContent,String fieldName, String fieldValue, String message) {
            this.lineNumber = lineNumber;
            this.lineContent = lineContent;
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
            this.message = message;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public String[] getLineContent() {
            return lineContent;
        }

        public void setLineContent(String[] lineContent) {
            this.lineContent = lineContent;
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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
