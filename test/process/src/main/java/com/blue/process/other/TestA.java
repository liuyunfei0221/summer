package com.blue.process.other;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestA {

    public static void main(String[] args) {
        String s = "eyJkYXRhIjp7Im9yZGVySWRzIjoiMTA0Njk2MDk5NDU4Nzc3MDkwIn19";

        byte[] decode = Base64.getDecoder().decode(s);

        String res = new String(decode, StandardCharsets.UTF_8);

        System.err.println(res);
    }

}
