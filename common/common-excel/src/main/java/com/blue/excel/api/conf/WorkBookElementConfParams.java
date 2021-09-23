package com.blue.excel.api.conf;

import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * excel元素封装
 *
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
@SuppressWarnings("unused")
public class WorkBookElementConfParams<T> implements WorkBookElementConf<T> {

    protected String[] headers;

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
