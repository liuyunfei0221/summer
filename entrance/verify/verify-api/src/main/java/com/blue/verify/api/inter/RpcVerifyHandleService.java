package com.blue.verify.api.inter;

import com.blue.base.constant.verify.BusinessType;
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
     * @param businessType
     * @param destination
     * @return
     */
    CompletableFuture<String> generate(VerifyType verifyType, BusinessType businessType, String destination);

    /**
     * validate verify
     *
     * @param verifyType
     * @param businessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    CompletableFuture<Boolean> validate(VerifyType verifyType, BusinessType businessType, String key, String verify, Boolean repeatable);

}
