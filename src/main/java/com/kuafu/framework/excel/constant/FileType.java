package com.kuafu.framework.excel.constant;

/**
 * Created by yangjiayong on 2016/7/26.
 */
public enum FileType {

    XLS,XLSX,CSV;

    public static FileType convertFromFileName(String fileName){
        if(fileName.endsWith(".csv")){
            return FileType.CSV;
        }else if(fileName.endsWith(".xls")){
            return FileType.XLS;
        }else if(fileName.endsWith(".xlsx")){
            return FileType.XLSX;
        }else {
            throw new IllegalArgumentException("File Type Error!!!");
        }
    }

}
