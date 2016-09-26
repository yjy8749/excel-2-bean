package com.kuafu.framework.excel.core;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public class CsvWriter implements Writer {

    private com.csvreader.CsvWriter writer = null;

    public CsvWriter(File file) throws IOException {
        this.writer = new com.csvreader.CsvWriter(new FileOutputStream(file),',', Charset.forName("UTF-8"));
    }

    public CsvWriter(File file,String charset) throws IOException {
        this.writer = new com.csvreader.CsvWriter(new FileOutputStream(file),',', Charset.forName(charset));
    }

    public CsvWriter(OutputStream outputStream){
        this.writer = new com.csvreader.CsvWriter(outputStream,',', Charset.forName("UTF-8"));
    }

    public CsvWriter(OutputStream outputStream,String charset){
        this.writer = new com.csvreader.CsvWriter(outputStream,',', Charset.forName(charset));
    }

    public CsvWriter(File file,char separator) throws IOException {
        this.writer = new com.csvreader.CsvWriter(new FileOutputStream(file),separator, Charset.forName("UTF-8"));
    }

    public CsvWriter(File file,char separator,String charset) throws IOException {
        this.writer = new com.csvreader.CsvWriter(new FileOutputStream(file),separator, Charset.forName(charset));
    }

    public CsvWriter(OutputStream outputStream,char separator) {
        this.writer = new com.csvreader.CsvWriter(outputStream,separator, Charset.forName("UTF-8"));
    }

    public CsvWriter(OutputStream outputStream,char separator,String charset){
        this.writer = new com.csvreader.CsvWriter(outputStream,separator, Charset.forName(charset));
    }

    @Override
    public void writeRecord(String[] values) throws IOException {
        this.writer.writeRecord(values);
    }

    @Override
    public void close() {
        this.writer.flush();
        this.writer.close();
    }
}
