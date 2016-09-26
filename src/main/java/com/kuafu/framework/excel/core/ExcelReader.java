package com.kuafu.framework.excel.core;

import com.kuafu.framework.excel.constant.FileType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public class ExcelReader implements Reader{

    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    private Workbook excel;
    private Sheet sheet;
    private Row row;

    /**
     * 当前数据所在行数，1表示第一行，-1表示未开始读取
     */
    private int currentRowIndex = -1;

    private Map<String,Integer> headerMap = new LinkedHashMap<String, Integer>();
    private String[] headerArray = null;

    public ExcelReader(File file,FileType type) throws IOException {
        switch (type){
            case XLS:
                excel = new HSSFWorkbook(new FileInputStream(file));break;
            default:
                excel = new XSSFWorkbook(new FileInputStream(file));
        }
        sheet = excel.getSheetAt(0);
    }

    public ExcelReader(InputStream inputStream, FileType type) throws IOException {
        switch (type){
            case XLS:
                excel = new HSSFWorkbook(inputStream);break;
            default:
                excel = new XSSFWorkbook(inputStream);
        }
        sheet = excel.getSheetAt(0);
    }

    public ExcelReader(File file,FileType type,String sheetName) throws IOException {
        switch (type){
            case XLS:
                excel = new HSSFWorkbook(new FileInputStream(file));break;
            default:
                excel = new XSSFWorkbook(new FileInputStream(file));
        }
        sheet = excel.getSheet(sheetName);
    }

    public ExcelReader(InputStream inputStream, FileType type,String sheetName) throws IOException {
        switch (type){
            case XLS:
                excel = new HSSFWorkbook(inputStream);break;
            default:
                excel = new XSSFWorkbook(inputStream);
        }
        sheet = excel.getSheet(sheetName);
    }

    public String getValue(Cell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        switch (xssfCell.getCellType()) {
            case XSSFCell.CELL_TYPE_NUMERIC:
                long longVal = Math.round(xssfCell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(xssfCell)) {
                    double d = xssfCell.getNumericCellValue();
                    Date date = HSSFDateUtil.getJavaDate(d);
                    return new SimpleDateFormat("yyyy-MM-dd").format(date);
                } else if (Double.parseDouble(longVal + ".0") == xssfCell.getNumericCellValue()) {
                    return String.valueOf(longVal);
                } else {
                    return String.valueOf(xssfCell.getNumericCellValue());
                }
            case XSSFCell.CELL_TYPE_STRING:
                return xssfCell.getStringCellValue().trim();
            case XSSFCell.CELL_TYPE_FORMULA:
                return new BigDecimal(xssfCell.getCachedFormulaResultType()).toString();
            case XSSFCell.CELL_TYPE_BLANK:
                return "";
            case XSSFCell.CELL_TYPE_BOOLEAN:
                return String.valueOf(xssfCell.getBooleanCellValue());
            case XSSFCell.CELL_TYPE_ERROR:
                return String.valueOf(xssfCell.getErrorCellValue());
            default:
                return "";
        }
    }

    @Override
    public void setHeaders(String[] headers) {
        this.headerArray = headers;
    }

    @Override
    public boolean readHeaders() throws IOException {
        this.currentRowIndex ++ ;
        Row headerRow = sheet.getRow(this.currentRowIndex);
        if (headerRow != null) {
            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                String value = this.getValue(headerRow.getCell(i));
                if(StringUtils.isNotEmpty(value)){
                    this.headerMap.put(value,Integer.valueOf(i));
                }
            }
        }
        this.headerArray = this.headerMap.keySet().toArray(new String[this.headerMap.size()]);
        return true;
    }

    @Override
    public String[] getHeaders() throws IOException {
        return this.headerArray;
    }

    @Override
    public String getHeader(int index) throws IOException {
        return this.headerArray[index];
    }

    @Override
    public boolean readRecord() throws IOException {
        this.currentRowIndex++;
        if (this.currentRowIndex <= this.sheet.getLastRowNum()) {
            this.row = this.sheet.getRow(this.currentRowIndex);
            return true;
        }
        return false;
    }

    @Override
    public boolean skipRecord() throws IOException {
        this.currentRowIndex++;
        return true;
    }

    @Override
    public String[] getValues() throws IOException {
        String[] values = new String[this.row.getLastCellNum()];
        for (int i = 0; i < values.length; i++) {
            values[i] = this.getValue(this.row.getCell(i));
        }
        return values;
    }

    @Override
    public String get(Integer index) throws IOException {
        return index==null?null:this.getValue(this.row.getCell(index));
    }

    @Override
    public String get(String header) throws IOException {
         return this.get(this.headerMap.get(header));
    }

    @Override
    public int getCurrentRowIndex() {
        return this.currentRowIndex;
    }

    @Override
    public void close() {
        try {
            this.excel.close();
        } catch (IOException e) {
            logger.error("Excel Close Exception:",e);
        }
    }

    @Override
    public int readToEnd() throws IOException {
        return this.sheet.getLastRowNum()+1;
    }


}
