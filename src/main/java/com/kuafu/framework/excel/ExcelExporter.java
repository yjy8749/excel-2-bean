package com.kuafu.framework.excel;

import com.kuafu.framework.excel.constant.FileType;
import com.kuafu.framework.excel.core.CsvWriter;
import com.kuafu.framework.excel.core.ExcelWriter;
import com.kuafu.framework.excel.core.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public class ExcelExporter<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExporter.class);

    private Writer writer;

    private ExcelBean<T> excelBean = null;
    private ReflectBean<T> reflectBean = null;
    private String[] fields = null;
    private String[] headers = null;

    public ExcelExporter(Class<T> type,File file) throws IOException {
        FileType fileType = null;
        if(file.getName().endsWith(".csv")){
            fileType = FileType.CSV;
        }else if(file.getName().endsWith(".xls")){
            fileType = FileType.XLS;
        }else if(file.getName().endsWith(".xlsx")){
            fileType = FileType.XLSX;
        }else {
            throw new IllegalArgumentException("File Type Error!!!");
        }
        this.initExporter(type,new FileOutputStream(file),fileType);
    }

    public ExcelExporter(Class<T> type,OutputStream outputStream, FileType fileType) throws IOException {
        this.initExporter(type,outputStream,fileType);
    }

    public void initExporter(Class<T> type,OutputStream outputStream, FileType fileType){
        switch (fileType){
            case CSV:
                this.writer = new CsvWriter(outputStream);
                break;
            case XLS:
            case XLSX:
                this.writer = new ExcelWriter(outputStream,fileType);
                break;
            default:
                throw new IllegalArgumentException("File Type Error!!!");
        }
        this.excelBean = ExcelBean.get(type);
        this.reflectBean =  ReflectBean.get(type);

        this.fields = this.excelBean.getExportFields().toArray(new String[0]);
        this.headers = new String[this.fields.length];
        for(int i=0;i<this.fields.length;i++){
            this.headers[i] = this.excelBean.getExportName(this.fields[i]);
        }
    }

    public <T> void toExcel(List<T> dataes){
        try {
            this.writeHeader();
            this.write(dataes);
        } catch (Exception e) {
            logger.error("Bean List Export To Excel:", e);
        } finally {
            this.close();
        }
    }

    public void writeHeader() throws IOException {
        this.writer.writeRecord(this.headers);
    }

    public <T> void write(List<T> dataes) throws IOException, InvocationTargetException {
        if(!dataes.isEmpty()) {
            for(T data : dataes){
                String[] values = new String[fields.length];
                for(int i=0;i<fields.length;i++){
                    values[i] = excelBean.getConvert().convertTo(reflectBean.invokeGetter(data,fields[i]),fields[i],headers[i]);
                }
                this.writer.writeRecord(values);
            }
        }
    }

    public void close(){
        this.writer.close();
    }

    public <T> void exportImportHeader(Class<T> tClass){
        try {
            this.writeHeader();
        } catch (Exception e) {
            logger.error("Bean List Export To Excel:", e);
        } finally {
            this.close();
        }
    }
}
