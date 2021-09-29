package com.blue.jwt.common;

/**
 * JWT processor interface
 *
 * @author DarkBlue
 */

@SuppressWarnings({"JavaDoc", "unused"})
public interface JwtProcessor<T> {

    /**
     * create jwt
     *
     * @param t
     * @return
     */
    String create(T t);

    /**
     * parse jwt
     *
     * @param jwtToken
     * @return
     */
    T parse(String jwtToken);

    /**
     * get the upper limit of the expiration time of jwt
     *
     * @return
     */
    long getMaxExpireMillis();

    /**
     * get the lower limit of the expiration time of jwt
     *
     * @return
     */
    long getMinExpireMillis();

}
