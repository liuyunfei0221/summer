package com.blue.auth.api.component.jwt.constant;

import com.blue.auth.api.model.MemberPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * payload converters
 *
 * @author blue
 */
public final class PayloadConverters {

    /**
     * member payload -> claims
     */
    public static final transient Function<MemberPayload, Map<String, String>> PAYLOAD_2_CLAIM_CONVERTER = p -> {
        Map<String, String> claims = new HashMap<>(8, 2.0f);
        claims.put("t", p.getGamma());
        claims.put("h", p.getKeyId());
        claims.put("i", p.getId());
        claims.put("n", p.getLoginType());
        claims.put("g", p.getDeviceType());
        claims.put("s", p.getLoginTime());

        return claims;
    };

    /**
     * claims -> member payload
     */
    public static final transient Function<Map<String, String>, MemberPayload> CLAIM_2_PAYLOAD_CONVERTER = c ->
            new MemberPayload(c.get("t"), c.get("h"), c.get("i"), c.get("n"), c.get("g"), c.get("s"));

}
