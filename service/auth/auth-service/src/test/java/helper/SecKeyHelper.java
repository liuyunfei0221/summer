package helper;

import java.util.LinkedList;
import java.util.List;

import static com.blue.jwt.constant.JwtConfSchema.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

/**
 * @author liuyunfei
 */
@SuppressWarnings("SameParameterValue")
public class SecKeyHelper {

    //@see com.blue.jwt.constant.JwtConfSchema
    private static void generateSingKey(int len) {
        if (len < SEC_KEY_STR_MIN.len || len > SEC_KEY_STR_MAX.len)
            throw new RuntimeException("secKey's len can't be less than " + SEC_KEY_STR_MIN.len + " or greater than " + SEC_KEY_STR_MAX.len);

        System.err.println("signKey = ");
        System.err.println(randomAlphanumeric(len));
    }

    private static void generateGammaSecrets(int len, int size) {
        if (len < GAMMA_KEY_STR_MIN.len || len > GAMMA_KEY_STR_MAX.len)
            throw new RuntimeException("gammaSecret element len can't be less than " + GAMMA_KEY_STR_MIN.len + " or greater than " + GAMMA_KEY_STR_MAX.len);

        if (size < GAMMA_SECRETS_MIN.len || size > GAMMA_SECRETS_MAX.len)
            throw new RuntimeException("gammaSecret's element count can't be less than " + GAMMA_SECRETS_MIN.len + " or greater than " + GAMMA_SECRETS_MAX.len);

        if (Integer.bitCount(size) != 1)
            throw new RuntimeException("gammaSecret's count must be power of 2");

        List<String> gammaSecrets = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            gammaSecrets.add(randomAlphanumeric(len));
        }

        System.err.println("gammaSecrets = ");
        System.err.println(gammaSecrets);
    }

    public static void main(String[] args) {
        generateSingKey(512);
        generateGammaSecrets(64, 256);
    }

}
