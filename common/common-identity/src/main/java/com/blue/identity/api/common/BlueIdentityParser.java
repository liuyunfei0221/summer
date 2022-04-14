package com.blue.identity.api.common;

import com.blue.identity.core.SnowflakeIdentityParser;
import com.blue.identity.model.IdentityElement;

/**
 * static id parser
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class BlueIdentityParser {

    /**
     * Parse id attribute
     *
     * @param id
     * @return
     */
    public static IdentityElement parse(long id) {
        return SnowflakeIdentityParser.parse(id);
    }

}
