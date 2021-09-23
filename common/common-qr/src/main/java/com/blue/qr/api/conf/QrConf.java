package com.blue.qr.api.conf;

import java.awt.*;

/**
 * 二维码配置参数封装
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc"})
public interface QrConf {

    /**
     * 宽
     *
     * @return
     */
    int getWidth();

    /**
     * 高
     *
     * @return
     */
    int getHeight();

    /**
     * 内部颜色
     *
     * @return
     */
    int getOnColor();

    /**
     * 外部颜色
     *
     * @return
     */
    int getOffColor();

    /**
     * 笔画宽度
     *
     * @return
     */
    int getStrokesWidth();

    /**
     * logo边框颜色
     *
     * @return
     */
    Color getLogoFrameColor();

    /**
     * 填充颜色
     *
     * @return
     */
    Color getFrameColor();

}
