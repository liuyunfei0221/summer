package test;

import java.util.Date;

/**
 * @author liuyunfei
 */
public class DataTest {

    public static void main(String[] args) {

        long l = System.currentTimeMillis();

        Date date = new Date(l);

        System.err.println(l == date.getTime());
    }

}
