package test;

import com.blue.base.common.base.LongEncoder;

public class OtherTest {

    public static void main(String[] args) {
        long a = 1341341234567L;

        LongEncoder longEncoder = new LongEncoder("asdfewqerqwerq");
        System.err.println("===================================================");


        Long encryptLong = longEncoder.encryptLong(a);
        System.err.println("encryptLong = " + encryptLong);

        Long decryptLong = longEncoder.decryptLong(encryptLong);
        System.err.println("decryptLong = " + decryptLong);

        System.err.println("===================================================");
        System.err.println(a == decryptLong);
    }

}
