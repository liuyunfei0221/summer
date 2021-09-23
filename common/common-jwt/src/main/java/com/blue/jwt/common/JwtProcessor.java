package com.blue.jwt.common;

/**
 * JWT处理接口
 *
 * @author DarkBlue
 */

@SuppressWarnings("JavaDoc")
public interface JwtProcessor<T> {

    /**
     * 创建jwt
     *
     * @param t
     * @return
     */
    String create(T t);

    /**
     * 解析jwt
     *
     * @param jwtToken
     * @return
     */
    T parse(String jwtToken);

    /**
     * 获取jwt过期时间上限值
     *
     * @return
     */
    long getMaxExpireMillis();

    /**
     * 获取jwt过期时间下限值
     *
     * @return
     */
    long getMinExpireMillis();

}
