package com.kuafu.framework.excel.core;

import java.io.IOException;

/**
 * Created by yangjiayong on 2016/7/27.
 */
public interface Writer {

    public void writeRecord(String[] var1) throws IOException;

    public void close();

}
