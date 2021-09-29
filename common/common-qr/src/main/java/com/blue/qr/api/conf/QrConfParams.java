package com.blue.qr.api.conf;

import java.awt.*;

/**
 * 默认二维码配置参数封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class QrConfParams implements QrConf {

    /**
     * qrcode size
     */
    private int width = 200;
    private int height = 200;

    /**
     * logo color
     */
    private int onColor = 0xFF000001;
    private int offColor = 0xFFFFFFFF;

    /**
     * strokes info
     */
    private int strokesWidth = 1;

    protected Color logoFrameColor = Color.WHITE;

    protected Color frameColor = Color.CYAN;

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
    public Color getLogoFrameColor() {
        return logoFrameColor;
    }

    @Override
    public Color getFrameColor() {
        return frameColor;
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

    public void setLogoFrameColor(Color logoFrameColor) {
        this.logoFrameColor = logoFrameColor;
    }

    public void setFrameColor(Color frameColor) {
        this.frameColor = frameColor;
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
                '}';
    }

}
