package helper;

import java.util.LinkedList;
import java.util.List;

import static com.blue.jwt.constant.JwtConfSchema.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

/**
 * @author DarkBlue
 * @date 2021/8/13
 * @apiNote
 */
@SuppressWarnings("SameParameterValue")
public class SecKeyHelper {

    //见JwtConfSchema配置

    private static void generateSingKey(int len) {
        if (len < SEC_KEY_STR_MIN.len || len > SEC_KEY_STR_MAX.len)
            throw new RuntimeException("secKey的字符长度不能低于 " + SEC_KEY_STR_MIN.len + " 或高于 " + SEC_KEY_STR_MAX.len);

        System.err.println("signKey = ");
        System.err.println(randomAlphanumeric(len));
    }

    private static void generateGammaSecrets(int len, int size) {
        if (len < GAMMA_KEY_STR_MIN.len || len > GAMMA_KEY_STR_MAX.len)
            throw new RuntimeException("gammaSecrets元素的字符长度不能低于 " + GAMMA_KEY_STR_MIN.len + " 或高于 " + GAMMA_KEY_STR_MAX.len);

        if (size < GAMMA_SECRETS_MIN.len || size > GAMMA_SECRETS_MAX.len)
            throw new RuntimeException("gammaSecret的元素数量不能低于 " + GAMMA_SECRETS_MIN.len + " 或高于 " + GAMMA_SECRETS_MAX.len);

        if (Integer.bitCount(size) != 1)
            throw new RuntimeException("gammaSecrets的元素数量必须为2的幂数");

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
