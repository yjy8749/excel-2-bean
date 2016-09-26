package com.kuafu.framework.excel;

import com.kuafu.framework.excel.constant.FileType;
import com.kuafu.framework.excel.core.CsvReader;
import com.kuafu.framework.excel.core.ExcelReader;
import com.kuafu.framework.excel.core.Reader;
import com.kuafu.framework.excel.utils.ExcelFieldConvertException;
import com.kuafu.framework.excel.utils.ExcelImportResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public class ExcelImporter<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExcelImporter.class);

    /**
     * 发送异常时执行处理类型
     */
    public static enum EXECUTION_TYPE_WHEN_EXCEPTION{
        /**
         * 继续处理
         */
        CONTINUE,
        /**
         * 中断并返回
         */
        BREAK_AND_RETURN,
        /**
         * 中断并抛出
         */
        BREAK_AND_THROWS;

    }



    private Reader reader;

    private ExcelBean<T> excelBean = null;
    private ReflectBean<T> reflectBean =null;

    private String[] headers = null;

    private EXECUTION_TYPE_WHEN_EXCEPTION executionTypeWhenException = EXECUTION_TYPE_WHEN_EXCEPTION.CONTINUE;

    public ExcelImporter(Class<T> type,File file) throws IOException {
        FileType fileType;
        if(file.getName().endsWith(".csv")){
            fileType = FileType.CSV;
        }else if(file.getName().endsWith(".xls")){
            fileType = FileType.XLS;
        }else if(file.getName().endsWith(".xlsx")){
            fileType = FileType.XLSX;
        }else {
            throw new IllegalArgumentException("File Type Error!!!");
        }
        this.initImporter(type,new FileInputStream(file),fileType);
    }

    public ExcelImporter(Class<T> type,InputStream inputStream, FileType fileType) throws IOException {
        this.initImporter(type,inputStream,fileType);
    }

    public void initImporter(Class<T> type,InputStream inputStream, FileType fileType) throws IOException {
        switch (fileType){
            case CSV:
                this.reader = new CsvReader(inputStream);
                break;
            case XLS:
            case XLSX:
                this.reader = new ExcelReader(inputStream,fileType);
                break;
            default:
                throw new IllegalArgumentException("File Type Error!!!");
        }

        this.excelBean = ExcelBean.get(type);
        this.reflectBean = ReflectBean.get(type);

        this.reader.readHeaders();

        this.headers = excelBean.getImportNames().toArray(new String[0]);
    }

    public EXECUTION_TYPE_WHEN_EXCEPTION getExecutionTypeWhenException() {
        return executionTypeWhenException;
    }

    public void setExecutionTypeWhenException(EXECUTION_TYPE_WHEN_EXCEPTION executionTypeWhenException) {
        this.executionTypeWhenException = executionTypeWhenException;
    }

    public ExcelImportResult<T> toList() throws Exception {
        return this.toList(null,null);
    }

    public ExcelImportResult<T> toList(int start) throws Exception {
        return this.toList(start,null);
    }

    public ExcelImportResult<T> toList(Integer start, Integer size) throws Exception {
        try {
            if(start !=null){
                while (this.reader.getCurrentRowIndex()+1<start){
                    this.reader.skipRecord();
                }
            }
            return this.readToList(size);
        } finally {
            this.close();
        }
    }

    public ExcelImportResult<T> readToList() throws Exception {
        return this.readToList(null);
    }

    public ExcelImportResult<T> readToList(Integer size) throws Exception {

        ExcelImportResult<T> result = new ExcelImportResult<T>();
        result.setHasNext(this.reader.readRecord());
        while (result.isHasNext()) {
            try {
                if (StringUtils.isNotEmpty(StringUtils.join(this.reader.getValues(), ""))){
                    T obj = reflectBean.newInstance();
                    for (String name : headers) {
                        String fieldName = excelBean.getImportField(name);
                        Object value = excelBean.getConvert().convertFrom(reflectBean.getFieldType(fieldName),this.reader.get(name),fieldName,name,excelBean.isRequired(fieldName));
                        reflectBean.invokeSetter(obj,fieldName,value);
                    }
                    result.getResults().add(obj);
                }
            }catch (ExcelFieldConvertException ex){
                if(executionTypeWhenException == EXECUTION_TYPE_WHEN_EXCEPTION.CONTINUE) {
                    result.getErrorList().add(new ExcelImportResult.FieldError(
                            this.reader.getCurrentRowIndex(), this.reader.getValues(), ex.getFieldName(), ex.getFieldValue(), ex.getMessage())
                    );
                }else if(executionTypeWhenException == EXECUTION_TYPE_WHEN_EXCEPTION.BREAK_AND_RETURN) {
                    result.getErrorList().add(new ExcelImportResult.FieldError(
                            this.reader.getCurrentRowIndex(), this.reader.getValues(), ex.getFieldName(), ex.getFieldValue(), ex.getMessage())
                    );
                    break;
                }else {
                    throw ex;
                }
            }
            if(size!=null&&(result.getResults().size()+result.getErrorList().size())>=size){
                break;
            }
            result.setHasNext(this.reader.readRecord());
        }
        return result;
    }

    public void close(){
        this.reader.close();
    }

    public int readToEnd() {
        try {
            return this.reader.readToEnd();
        } catch (Exception e) {
            logger.error("Excel Import Read To End Error:",e);
        }finally {
            this.reader.close();
        }
        return 0;
    }

    public int readToEndExcludeHead() {
        try {
            return this.reader.readToEnd()-1;
        } catch (Exception e) {
            logger.error("Excel Import Read To End Error:",e);
        }finally {
            this.reader.close();
        }
        return 0;
    }
}
