package com.blue.identity.api.common;

import java.util.UUID;

/**
 * uuid generator
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class BlueUuidGenerator {

    /**
     * Generate uuid
     *
     * @return
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }

}
