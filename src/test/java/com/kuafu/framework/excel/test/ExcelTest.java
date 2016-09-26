package com.kuafu.framework.excel.test;

import com.kuafu.framework.excel.ExcelExporter;
import com.kuafu.framework.excel.ExcelImporter;
import com.kuafu.framework.excel.ExcelTestBase;
import com.kuafu.framework.excel.utils.ExcelImportResult;
import com.kuafu.framework.excel.utils.TestBean;
import com.kuafu.framework.excel.utils.TestType;
import org.junit.Test;
import org.springframework.util.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public class ExcelTest extends ExcelTestBase {

    public List<TestBean> getBeanList(){
        List<TestBean> beanList = new ArrayList<TestBean>();
        for(int i=0;i<50;i++){
            TestBean bean = new TestBean();
            bean.setName("name:"+(i+1));
            bean.setTestType(i%2==0? TestType.ONE:TestType.TWO);
            beanList.add(bean);
        }
        return beanList;
    }

    @Test
    public void testExport1() throws Exception {

        List<TestBean> beanList = this.getBeanList();

        File exportFile = new File("aaaa.xlsx");
        new ExcelExporter(TestBean.class,exportFile).toExcel(beanList);
        Assert.isTrue(exportFile.exists());
        exportFile.delete();

    }

    @Test
    public void testExport2() throws Exception {

        List<TestBean> beanList = this.getBeanList();

        File exportFile = new File("aaaa.xlsx");
        ExcelExporter exporter = new ExcelExporter(TestBean.class,exportFile);

        try{
            exporter.writeHeader();
            exporter.write(beanList);
            exporter.write(beanList);
        }finally {
            exporter.close();
        }
        Assert.isTrue(exportFile.exists());
        exportFile.delete();
    }

    @Test
    public void testImport1() throws Exception {
        List<TestBean> beanList = this.getBeanList();

        File exportFile = new File("aaaa.xlsx");
        new ExcelExporter(TestBean.class,exportFile).toExcel(beanList);
        Assert.isTrue(exportFile.exists());

        ExcelImporter importer = new ExcelImporter(TestBean.class,exportFile);
        ExcelImportResult result = importer.toList();
        Assert.isTrue(result.isAllSuccess());
        exportFile.delete();

    }

    @Test
    public void testImport2() throws Exception {
        List<TestBean> beanList = this.getBeanList();

        File exportFile = new File("aaaa.xlsx");
        new ExcelExporter(TestBean.class,exportFile).toExcel(beanList);
        Assert.isTrue(exportFile.exists());

        ExcelImporter importer = new ExcelImporter(TestBean.class,exportFile);
        try {
            ExcelImportResult result = importer.readToList(20);
            Assert.isTrue(result.isAllSuccess());
            result = importer.readToList(20);
            Assert.isTrue(result.isAllSuccess());
            result = importer.readToList(20);
            Assert.isTrue(result.isAllSuccess());
        }finally {
            importer.close();
        }
        exportFile.delete();

    }
}
