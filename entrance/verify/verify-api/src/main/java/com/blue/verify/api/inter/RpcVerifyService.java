package com.blue.verify.api.inter;

import com.blue.base.constant.base.RandomType;
import com.blue.verify.api.model.VerifyPair;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * rpc verify interface
 *
 * @author DarkBlue
 * @date 2021/8/12
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface RpcVerifyService {

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @param expire
     * @return
     */
    CompletableFuture<VerifyPair> generate(RandomType type, int length, Duration expire);

    /**
     * validate pair
     *
     * @param verifyPair
     * @param repeatable
     * @return
     */
    CompletableFuture<Boolean> validate(VerifyPair verifyPair, boolean repeatable);

}
