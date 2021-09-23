package com.blue.excel.api.generator;

import com.blue.excel.api.conf.WorkBookElementConf;
import com.blue.excel.api.conf.WorkBookStyleConf;
import com.blue.excel.common.WorkBookProcessor;

/**
 * Excel工具构建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class BlueWorkBookProcessorGenerator {

    /**
     * 创建WorkBookGenerator
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
