package com.blue.verify.api.inter;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc verify handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface RpcVerifyHandleService {

    /**
     * generate verify for api
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param destination
     * @param languages
     * @return
     */
    CompletableFuture<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination, List<String> languages);

    /**
     * validate verify
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    CompletableFuture<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable);

    /**
     * validate by turing test
     *
     * @param identity
     * @param allow
     * @param expiresMillis
     * @param key
     * @param verify
     * @return
     */
    CompletableFuture<Boolean> turingValidate(String identity, Integer allow, Long expiresMillis, String key, String verify);
}
