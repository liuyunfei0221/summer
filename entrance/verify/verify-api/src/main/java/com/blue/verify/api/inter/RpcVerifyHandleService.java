package com.blue.verify.api.inter;

import com.blue.base.constant.verify.VerifyBusinessType;
import com.blue.base.constant.verify.VerifyType;

import java.util.concurrent.CompletableFuture;

/**
 * rpc verify handler interface
 *
 * @author DarkBlue
 * @date 2021/8/12
 * @apiNote
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

}
