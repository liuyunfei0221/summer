package com.blue.excel.api.conf;

import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * excel elements params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class WorkBookElementConfParams<T> implements WorkBookElementConf<T> {

    /**
     * headers
     *
     * @return
     */
    protected String[] headers;

    /**
     * elements in data list to excel row converter
     *
     * @return
     */
    BiConsumer<XSSFRow, T> rowElementPackager;

    public WorkBookElementConfParams() {
    }

    public WorkBookElementConfParams(String[] headers, BiConsumer<XSSFRow, T> rowElementPackager) {
        this.headers = headers;
        this.rowElementPackager = rowElementPackager;
    }

    @Override
    public String[] getHeaders() {
        return headers;
    }

    @Override
    public BiConsumer<XSSFRow, T> getRowElementPackager() {
        return rowElementPackager;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public void setRowElementPackager(BiConsumer<XSSFRow, T> rowElementPackager) {
        this.rowElementPackager = rowElementPackager;
    }

    @Override
    public String toString() {
        return "WorkBookElementConfParams{" +
                "headers=" + Arrays.toString(headers) +
                ", rowElementPackager=" + rowElementPackager +
                '}';
    }

}
