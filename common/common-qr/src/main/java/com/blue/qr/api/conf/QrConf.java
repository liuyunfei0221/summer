package com.blue.qr.api.conf;

/**
 * qr conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public interface QrConf {

    /**
     * width
     *
     * @return
     */
    int getWidth();

    /**
     * height
     *
     * @return
     */
    int getHeight();

    /**
     * on color
     *
     * @return
     */
    int getOnColor();

    /**
     * off color
     *
     * @return
     */
    int getOffColor();

    /**
     * strokes width
     *
     * @return
     */
    int getStrokesWidth();

    /**
     * logo frame color
     *
     * @return
     */
    ColorAttr getLogoFrameColor();

    /**
     * frame color
     *
     * @return
     */
    ColorAttr getFrameColor();

    /**
     * qr code file type
     *
     * @return
     */
    String getFileType();

}
