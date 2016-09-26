package com.kuafu.framework.excel.core;

import java.io.IOException;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public interface Reader {

    public void setHeaders(String[] headers);

    public boolean readHeaders() throws IOException;

    public String[] getHeaders() throws IOException;

    public String getHeader(int index) throws IOException;

    public boolean readRecord() throws IOException;

    public boolean skipRecord() throws IOException;

    public String[] getValues() throws IOException;

    public String get(Integer index) throws IOException;

    public String get(String header) throws IOException;

    public int getCurrentRowIndex();

    public void close();

    public int readToEnd() throws IOException;
}
