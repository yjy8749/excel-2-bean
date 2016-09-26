package com.kuafu.framework.excel.core;

import com.kuafu.framework.excel.constant.FileType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public class ExcelWriter implements Writer {

    private Workbook excel;
    private Sheet sheet;
    private OutputStream outputStream;
    private int currentRowIndex = 0;

    public ExcelWriter(File file, FileType type) throws IOException {
        switch (type){
            case XLS:
                this.excel = new HSSFWorkbook();break;
            default:
                this.excel = new XSSFWorkbook();
        }
        this.outputStream = new FileOutputStream(file);
        this.sheet = this.excel.createSheet();
    }

    public ExcelWriter(OutputStream outputStream, FileType type){
        switch (type){
            case XLS:
                this.excel = new HSSFWorkbook();break;
            default:
                this.excel = new XSSFWorkbook();
        }
        this.outputStream = outputStream;
        this.sheet = this.excel.createSheet();
    }

    public ExcelWriter(File file, FileType type,String sheetName) throws IOException {
        switch (type){
            case XLS:
                this.excel = new HSSFWorkbook();break;
            default:
                this.excel = new XSSFWorkbook();
        }
        this.outputStream = new FileOutputStream(file);
        this.sheet = this.excel.createSheet(sheetName);
    }

    public ExcelWriter(OutputStream outputStream, FileType type,String sheetName){
        switch (type){
            case XLS:
                this.excel = new HSSFWorkbook();break;
            default:
                this.excel = new XSSFWorkbook();
        }
        this.outputStream = outputStream;
        this.sheet = this.excel.createSheet(sheetName);
    }

    @Override
    public void writeRecord(String[] values) throws IOException {
        Row row = this.sheet.createRow(this.currentRowIndex++);
        Cell cell = null;
        for(int i=0;i<values.length;i++){
            cell = row.createCell(i,Cell.CELL_TYPE_STRING);
            cell.setCellValue(values[i]);
        }
    }

    @Override
    public void close() {
        try {
            this.excel.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                this.outputStream.flush();
                this.outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
