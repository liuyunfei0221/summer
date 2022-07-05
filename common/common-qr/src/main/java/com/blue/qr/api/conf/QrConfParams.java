package com.blue.qr.api.conf;

import static java.awt.Color.CYAN;
import static java.awt.Color.WHITE;

/**
 * default qr conf params
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class QrConfParams implements QrConf {

    /**
     * qrcode size
     */
    protected int width = 200;
    protected int height = 200;

    /**
     * logo color
     */
    protected int onColor = 0xFF000001;
    protected int offColor = 0xFFFFFFFF;

    /**
     * strokes info
     */
    protected int strokesWidth = 1;

    protected ColorAttr logoFrameColor = new ColorAttr(WHITE.getRed(), WHITE.getGreen(), WHITE.getBlue(), WHITE.getAlpha());

    protected ColorAttr frameColor = new ColorAttr(CYAN.getRed(), CYAN.getGreen(), CYAN.getBlue(), CYAN.getAlpha());

    protected String fileType;

    public QrConfParams() {
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getOnColor() {
        return onColor;
    }

    @Override
    public int getOffColor() {
        return offColor;
    }

    @Override
    public int getStrokesWidth() {
        return strokesWidth;
    }

    @Override
    public ColorAttr getLogoFrameColor() {
        return logoFrameColor;
    }

    @Override
    public ColorAttr getFrameColor() {
        return frameColor;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setOnColor(int onColor) {
        this.onColor = onColor;
    }

    public void setOffColor(int offColor) {
        this.offColor = offColor;
    }

    public void setStrokesWidth(int strokesWidth) {
        this.strokesWidth = strokesWidth;
    }

    public void setLogoFrameColor(ColorAttr logoFrameColor) {
        this.logoFrameColor = logoFrameColor;
    }

    public void setFrameColor(ColorAttr frameColor) {
        this.frameColor = frameColor;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "QrConfParams{" +
                "width=" + width +
                ", height=" + height +
                ", onColor=" + onColor +
                ", offColor=" + offColor +
                ", strokesWidth=" + strokesWidth +
                ", logoFrameColor=" + logoFrameColor +
                ", frameColor=" + frameColor +
                ", fileType=" + fileType +
                '}';
    }

}
