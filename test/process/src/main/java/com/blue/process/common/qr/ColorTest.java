package com.blue.process.common.qr;

import static java.awt.Color.CYAN;

public class ColorTest {

    public static void main(String[] args) {

        System.err.println(CYAN.getRed());
        System.err.println(CYAN.getGreen());
        System.err.println(CYAN.getBlue());
        System.err.println(CYAN.getAlpha());

        System.err.println("==========");

        System.err.println(0xFF000001);
        System.err.println(0xFFFFFFFF);
    }

}
