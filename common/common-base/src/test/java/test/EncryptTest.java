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
        //本地生成测试数据
        String priKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAyEXfJTvIkt6LFAwgB0Y680MdJsPVwchT7BDzLzfXNLUKlldiLkA+GMGtn30gT0EenvK0XoteYOWKNwjbb39W6QIDAQABAkACXLieEs9x1Agl62KxU0W8uszsOSEbUQaUYTrNEsaKC5c/kpCtPQF5RogNJp3t8xZEheVLubJQtBjDCUh6iyBVAiEA+WairwsXgXRnMqkAsGA0oxlkhQhU1MOWm9ewNrp+10MCIQDNknVcw+KfZ78MyTmDSR+ebKm7jNwc2xoeribL1+gIYwIgTlA3N2XxPJozqYm+CKQ9AxX/JXLnSGQeT9NbpuPK3ocCIFyr8YxoIfmKY234KJ0ukbpxcfLChVfczeGW0JaENm3RAiEAq3zKC372212F829qb0jWCfILT9ohptNZq0zJogp+t0w=";
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
