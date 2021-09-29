package com.blue.excel.api.generator;

import com.blue.excel.api.conf.WorkBookElementConf;
import com.blue.excel.api.conf.WorkBookStyleConf;
import com.blue.excel.common.WorkBookProcessor;

/**
 * workbook process generator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class BlueWorkBookProcessorGenerator {

    /**
     * generate workBook processor
     *
     * @param workBookElementConf
     * @param workBookStyleConf
     * @param <T>
     * @return
     */
    public static <T> WorkBookProcessor<T> createWorkBookGenerator(WorkBookElementConf<T> workBookElementConf, WorkBookStyleConf workBookStyleConf) {
        return new WorkBookProcessor<>(workBookElementConf, workBookStyleConf);
    }

}
