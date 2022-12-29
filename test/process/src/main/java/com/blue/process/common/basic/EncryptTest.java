package com.blue.process.common.basic;

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
        String priKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAlT15BNjtReILW61iL7o7W2OM69htAjf70A+Ym9tJUqlExH9M5BnZK+1wTFei9cKrzOZAsjc+uQzv5/zm2152BwIDAQABAkEAggxIad3ynWQGC8SB/B8n3FJamaZGD5njWAJPNPmY4msBuyQ7bVkOZU+4H48kAqK3lNccYP1dM6NtWSjisNM/sQIhANm3XpyhUSKRfaZS2CUuuW/RimooD4DnjNEhZOXk/UBPAiEAr3ubfij9GUhmKaoj71wqkiLSKUezdeTmCbavC5eTiMkCIQCGXBjidte4iqtNguDriXtdW6adPt3agouv4HkBO6FjFwIhAJG+yMtEgcmg3vEV1Vi0pMrQztnoKj3tib1hlAq9rpQ5AiB3y6zi4aqru3J09JkxPkAEoegGMevDBl562U2hQqE5Pw==";
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
