package com.kuafu.framework.excel.core;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public class CsvReader implements Reader {

    private com.csvreader.CsvReader reader = null;

    /**
     * 当前数据所在行数，1表示第一行，-1表示未开始读取
     */
    private int currentRowIndex = -1;

    public CsvReader(File file) throws IOException {
        this.reader = new com.csvreader.CsvReader(new FileInputStream(file),',', Charset.forName("UTF-8"));
    }

    public CsvReader(File file,String charset) throws IOException {
        this.reader = new com.csvreader.CsvReader(new FileInputStream(file),',', Charset.forName(charset));
    }

    public CsvReader(InputStream inputStream){
        this.reader = new com.csvreader.CsvReader(inputStream,',', Charset.forName("UTF-8"));
    }

    public CsvReader(InputStream inputStream,String charset){
        this.reader = new com.csvreader.CsvReader(inputStream,',', Charset.forName(charset));
    }

    public CsvReader(File file,char separator) throws IOException {
        this.reader = new com.csvreader.CsvReader(new FileInputStream(file),separator, Charset.forName("UTF-8"));
    }

    public CsvReader(File file,char separator,String charset) throws IOException {
        this.reader = new com.csvreader.CsvReader(new FileInputStream(file),separator, Charset.forName(charset));
    }

    public CsvReader(InputStream inputStream,char separator){
        this.reader = new com.csvreader.CsvReader(inputStream,separator, Charset.forName("UTF-8"));
    }

    public CsvReader(InputStream inputStream,char separator,String charset){
        this.reader = new com.csvreader.CsvReader(inputStream,separator, Charset.forName(charset));
    }

    @Override
    public void setHeaders(String[] headers) {
        this.reader.setHeaders(headers);
    }

    @Override
    public boolean readHeaders() throws IOException {
        this.currentRowIndex += 1;
        return this.reader.readHeaders();
    }

    @Override
    public String[] getHeaders() throws IOException {
        return this.reader.getHeaders();
    }

    @Override
    public String getHeader(int index) throws IOException {
        return this.reader.getHeader(index);
    }

    @Override
    public boolean readRecord() throws IOException {
        this.currentRowIndex += 1;
        return this.reader.readRecord();
    }

    @Override
    public boolean skipRecord() throws IOException {
        this.currentRowIndex += 1 ;
        return this.reader.skipRecord();
    }

    @Override
    public String[] getValues() throws IOException {
        return this.reader.getValues();
    }

    @Override
    public String get(Integer index) throws IOException {
        return index==null?null:this.reader.get(index);
    }

    @Override
    public String get(String header) throws IOException {
        return this.reader.get(header);
    }

    @Override
    public int getCurrentRowIndex() {
        return this.currentRowIndex;
    }

    @Override
    public void close() {
        this.reader.close();
    }

    @Override
    public int readToEnd() throws IOException {
        int line = 0;
        while (this.reader.readRecord()){
            line += 1;
        }
        return line;
    }
}
