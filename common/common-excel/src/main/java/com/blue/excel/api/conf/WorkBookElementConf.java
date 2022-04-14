package com.blue.excel.api.conf;

import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.function.BiConsumer;

/**
 * excel elements conf
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface WorkBookElementConf<T> {

    /**
     * headers
     *
     * @return
     */
    String[] getHeaders();

    /**
     * elements in data list to excel row converter
     *
     * @return
     */
    BiConsumer<XSSFRow, T> getRowElementPackager();

}
