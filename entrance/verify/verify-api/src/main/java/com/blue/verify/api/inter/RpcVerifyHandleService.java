package com.blue.verify.api.inter;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;

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
     * @return
     */
    CompletableFuture<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination);

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
     * @param key
     * @param verify
     * @return
     */
    CompletableFuture<Boolean> turingValidate(String key, String verify);
}
