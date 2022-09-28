package test;

import com.blue.basic.common.base.RsaProcessor;
import com.blue.basic.model.common.KeyPair;

import java.nio.charset.StandardCharsets;
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
        String priKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAxGi5VKZJcTZTx0DNNOAxYzkxOn/hWb5i0d5iU904vi7UN4nYRAwvAFVIDsIHxc40D8X2Bj+Vn8Zp/5kWFSiRXQIDAQABAkAPbNYZUtHzxTuBK5KyM2VNkKFQIdj17mDecKOySG57aRjYXPvoPZkI5MW7gwbKMtd2WqVuUojmp+Dx0XTi1jDhAiEA62HOrKIoSDo4RgnulAveMPvc6egCSXiaMjWu+L7ugHUCIQDVnP0or8qrtfVJOqd4CtRTMgvGYy14QsnB03G23NAwSQIhAK2HFrGofl/5I+Fmkw9rzGt6UrvSglUd5zea4hqZ0AS9AiEAq05Lo25evReeDszv8o/UuKdg1AdDNrdw73SvM/OfLakCIQCR6xqJREQ6qpExhdMA0re56G7sFL43w+RUXjBUTMvAJQ==";
        String original = "{\"original\":\"{\\\"amount\\\":400000,\\\"bankCardId\\\":8888888888,\\\"remark\\\":\\\"提现到钱包中\\\"}\",\"timeStamp\":" + Instant.now().getEpochSecond() + "}";

        String encrypt = generateTestParam(original, priKey);
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

        String data = "我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG. 我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG. 我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG. 我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG. 我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.我就随便写点啥ABCDEFG.";
//        String data = "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS";
        System.err.println(data.getBytes(StandardCharsets.UTF_8).length);

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
        System.err.println("===================================================================");

        sign = RsaProcessor.sign(data, priKey);
        System.err.println("sign = " + sign);
        System.err.println("verify = " + RsaProcessor.verify(data, sign, pubKey));
    }

}
