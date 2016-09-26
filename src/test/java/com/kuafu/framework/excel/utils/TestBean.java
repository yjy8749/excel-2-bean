package com.kuafu.framework.excel.utils;

import com.kuafu.framework.excel.annotation.ExcelConvert;
import com.kuafu.framework.excel.annotation.ExcelField;


/**
 * Created by yangjiayong on 2016/7/27.
 */
@ExcelConvert(value = TestConvert.class,singleton = true)
public class TestBean{

    @ExcelField("接收人")
    private String name;

    @ExcelField("测试类型")
    private TestType testType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }
}
