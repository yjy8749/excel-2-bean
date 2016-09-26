# Excel-2-bean
Encapsulation Excel(.xls,.xlsx,.csv) To Bean And Bean To Excel

# Dependencies

    compile 'org.apache.poi:poi'
    compile 'org.apache.poi:poi-ooxml'
    compile 'net.sourceforge.javacsv:javacsv:2.0'

# Usage

    ExcelImporter importer = new ExcelImporter(SomeBean.class,new File("..."));
    importer.readToList()
    importer.toList()

    ExcelExporter exporter = new ExcelExporter(SomeBean.class,new File("..."));
    exporter.toExcel(dataList);

# Junit Test Case

    com.kuafu.framework.excel.test.ExcelTest

# Other

Excel To Bean And Bean To Excel,Config By Annotation And Support Custom Converter