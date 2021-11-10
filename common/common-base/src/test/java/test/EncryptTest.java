package test;

import com.blue.base.common.base.RsaProcessor;
import com.blue.base.model.base.KeyPair;

import java.time.Instant;

/**
 * @author encrype test
 */
@SuppressWarnings("ALL")
public class EncryptTest {

    @SuppressWarnings({"AlibabaRemoveCommentedCode", "CommentedOutCode"})
    public static void main(String[] args) {
        debugging();
//        testDev();
    }

    /**
     * debug
     */
    private static void debugging() {
        String priKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAlYZ4AfKIePALHzUaoOy8H7e6T5W9681e+I3PQRc3KvUT+Q7X9wyyAMSJHIGjHrfhDKN+3ozTY9PfWUQLtsVNMQIDAQABAkA8W66f4p5yazuiGlaodUevagP8Uk77G8jlfqEV/mAXp35hPHhfELIl8WDlVpCaQNeLlCADJEJjJEdgHZ9C3mIBAiEA0M1Pd1Oj4BGFLrIJYVpQO9aL+/NtRZtxfKEjqs0eQmECIQC3UwIBkXLaU+35B/58CHUjA/MmYGTVrXT9NyJ3EV6c0QIgWemfJ5Y3whwpSqNV0LswQ6QuoW8AhfHa270T+8/aEOECIElJFrk3wvlCqhRuvK4q12DrGE3UJRtCn00GuHFu8SqxAiEAts5Zgvxbm0dpqybjJIPaeWTTDHvq+c9hE/jMDybs28U=";
        String original = "{\"original\":\"{\\\"amount\\\":400000,\\\"bankCardId\\\":8888888888,\\\"remark\\\":\\\"提现到钱包中\\\"}\",\"timeStamp\":" + Instant.now().getEpochSecond() + "}";

        String encrypt = generateTestParam(original, priKey);
//        parstTestParam(encrypt, priKey);
    }

    /**
     * encryption params for rest test
     */
    private static String generateTestParam(String original, String priKey) {

        String encrypt = RsaProcessor.encryptByPrivateKey(original, priKey);
        String sign = RsaProcessor.sign(encrypt, priKey);

        System.err.println("encrypt = ");
        System.err.println(encrypt);
        System.err.println("-------------------------");
        System.err.println("sign = ");
        System.err.println(sign);

        return encrypt;
    }

    /**
     * decrypt test
     */
    private static void parstTestParam(String encrypt, String priKey) {
        System.err.println("encrypt = ");
        System.err.println(RsaProcessor.decryptByPrivateKey(encrypt, priKey));
    }

    /**
     * for debugging on develop
     *
     * @param data
     * @param pubKey
     * @param priKey
     */
    public static void testDev() {

        String data = "我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG. 我就随便写点啥ABCDEFG.";

        KeyPair keyPair = RsaProcessor.initKeyPair();
        String pubKey = keyPair.getPubKey();
        String priKey = keyPair.getPriKey();

        System.err.println(pubKey);
        System.err.println(priKey);
        System.err.println("===================================================================");

        String secData = RsaProcessor.encryptByPrivateKey(data, priKey);
        System.err.println("secDataByPri---" + secData);
        System.err.println("data---" + RsaProcessor.decryptByPublicKey(secData, pubKey));
        System.err.println("===================================================================");

        secData = RsaProcessor.encryptByPublicKey(data, pubKey);
        System.err.println("secDataByPub---" + secData);
        System.err.println("data---" + RsaProcessor.decryptByPrivateKey(secData, priKey));
        System.err.println("===================================================================");

        String sign = RsaProcessor.sign(secData, priKey);
        System.err.println("sign = " + sign);
        System.err.println("verify = " + RsaProcessor.verify(secData, sign, pubKey));
    }

}
