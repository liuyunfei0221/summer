package com.blue.auth.api.component.jwt.api.generator;

import com.blue.auth.api.component.jwt.api.conf.MemberJwtConf;
import com.blue.auth.api.model.MemberPayload;
import com.blue.jwt.api.conf.BaseJwtConfParams;
import com.blue.jwt.api.generator.BlueJwtProcessorGenerator;
import com.blue.jwt.common.JwtProcessor;
import org.slf4j.Logger;

import java.util.Map;
import java.util.function.Function;

import static com.blue.auth.api.component.jwt.constant.PayloadConverters.CLAIM_2_PAYLOAD_CONVERTER;
import static com.blue.auth.api.component.jwt.constant.PayloadConverters.PAYLOAD_2_CLAIM_CONVERTER;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Member JwtProcessor generator
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public final class BlueMemberJwtProcessorGenerator {

    private static final Logger LOGGER = getLogger(BlueMemberJwtProcessorGenerator.class);

    /**
     * generate jwt processor
     *
     * @param memberJwtConf
     * @return
     */
    public static JwtProcessor<MemberPayload> generate(MemberJwtConf memberJwtConf) {
        LOGGER.info("JwtProcessor<MemberPayload> create(MemberJwtConf memberJwtConf), memberJwtConf = {}", memberJwtConf);

        BaseJwtConfParams<MemberPayload> baseJwtConfParams = new BaseJwtConfParams<>(memberJwtConf.getGlobalMaxExpireMillis(),
                memberJwtConf.getGlobalMinExpireMillis(), memberJwtConf.getSignKey(), memberJwtConf.getGammaSecrets()) {

            @Override
            public Function<MemberPayload, Map<String, String>> getDataToClaimProcessor() {
                return PAYLOAD_2_CLAIM_CONVERTER;
            }

            @Override
            public Function<Map<String, String>, MemberPayload> getClaimToDataProcessor() {
                return CLAIM_2_PAYLOAD_CONVERTER;
            }
        };

        return BlueJwtProcessorGenerator.generate(baseJwtConfParams);
    }

}
