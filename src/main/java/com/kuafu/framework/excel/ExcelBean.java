package com.kuafu.framework.excel;

import com.kuafu.framework.excel.annotation.ExcelConvert;
import com.kuafu.framework.excel.annotation.ExcelField;
import com.kuafu.framework.excel.utils.ExcelConvertInterface;
import com.kuafu.framework.excel.utils.SimpleExcelConvert;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public class ExcelBean<T> {

    private static final Map<Class,ExcelBean> EXCEL_BEAN_MAP = new HashMap<Class,ExcelBean>();

    private static final Map<Class,ExcelConvertInterface> EXCEL_CONVERT_INTERFACE_MAP = new HashMap<Class,ExcelConvertInterface>();

    private Map<String,String> importFieldMap = new LinkedHashMap<String,String>();
    private Map<String,String> exportNameMap = new LinkedHashMap<String,String>();
    private Set<String> requiredFieldSet = new HashSet<String>();

    private ExcelConvertInterface convert = null;

    static {
        EXCEL_CONVERT_INTERFACE_MAP.put(SimpleExcelConvert.class,new SimpleExcelConvert());
    }

    public static <T> ExcelBean<T> get(Class<T> objectClass){
        ExcelBean<T> excelBean = ExcelBean.EXCEL_BEAN_MAP.get(objectClass);
        if(excelBean == null){
            synchronized (ExcelBean.class){
                excelBean = ExcelBean.EXCEL_BEAN_MAP.get(objectClass);
                if(excelBean == null){
                    excelBean = new ExcelBean<T>(objectClass);
                    ExcelBean.EXCEL_BEAN_MAP.put(objectClass,excelBean);
                }
            }
        }
        return excelBean;
    }

    private ExcelBean(Class<T> objectClass){

        List<Field> fields = new ArrayList<Field>();

        for(Field field:objectClass.getDeclaredFields()){
            fields.add(field);
        }
        for(Class<?> classs : ClassUtils.getAllSuperclasses(objectClass)){
            for(Field field:classs.getDeclaredFields()){
                fields.add(field);
            }
        }

        for(Field field:fields){
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if(excelField==null){
                importFieldMap.put(field.getName(),field.getName());
                exportNameMap.put(field.getName(),field.getName());
            }else{
                String defName = StringUtils.isEmpty(excelField.value())?field.getName():excelField.value();
                if(excelField.imported()){
                    importFieldMap.put(defName,field.getName());
                }
                if(excelField.exported()){
                    exportNameMap.put(field.getName(),defName);
                }
                if(excelField.importRequired()){
                    requiredFieldSet.add(field.getName());
                }
            }
        }

        ExcelConvert excelConvert = objectClass.getAnnotation(ExcelConvert.class);
        if(excelConvert != null){
            try {
                if(excelConvert.singleton()){
                    synchronized (EXCEL_CONVERT_INTERFACE_MAP){
                        if(EXCEL_CONVERT_INTERFACE_MAP.containsKey(excelConvert.value())){
                            this.convert = EXCEL_CONVERT_INTERFACE_MAP.get(excelConvert.value());
                        }else{
                            this.convert = excelConvert.value().newInstance();
                            EXCEL_CONVERT_INTERFACE_MAP.put(excelConvert.value(),this.convert);
                        }
                    }
                }else{
                    this.convert = excelConvert.value().newInstance();
                }
            }catch (Throwable ex){
                throw new RuntimeException(ex);
            }
        }else{
            this.convert = EXCEL_CONVERT_INTERFACE_MAP.get(SimpleExcelConvert.class);
        }
    }

    public String getImportField(String name){
        return this.importFieldMap.get(name);
    }

    public Collection<String> getImportFields(){
        return this.importFieldMap.values();
    }

    public Collection<String> getImportNames(){
        return this.importFieldMap.keySet();
    }

    public String getExportName(String field){
        return this.exportNameMap.get(field);
    }

    public Collection<String> getExportNames(){
        return this.exportNameMap.values();
    }

    public Collection<String> getExportFields(){
        return this.exportNameMap.keySet();
    }

    public boolean isRequired(String name){
        return this.requiredFieldSet.contains(name);
    }

    public ExcelConvertInterface getConvert() {
        return convert;
    }
}
