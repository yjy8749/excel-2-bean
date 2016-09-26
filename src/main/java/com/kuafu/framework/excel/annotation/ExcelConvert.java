package com.kuafu.framework.excel.annotation;

import com.kuafu.framework.excel.utils.ExcelConvertInterface;
import com.kuafu.framework.excel.utils.SimpleExcelConvert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yangjiayong on 2016/9/23.
 */
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelConvert {

    public Class<? extends ExcelConvertInterface> value() default SimpleExcelConvert.class;

    public boolean singleton() default false;

}
