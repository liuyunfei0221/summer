package com.blue.qr.api.conf;

import java.io.Serializable;

/**
 * color rgb
 *
 * @author liuyunfei
 */
public final class ColorAttr implements Serializable {

    private static final long serialVersionUID = 5630806980968939301L;

    private int r;
    private int g;
    private int b;
    private int a;

    public ColorAttr() {
    }

    public ColorAttr(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "ColorAttr{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }

}
