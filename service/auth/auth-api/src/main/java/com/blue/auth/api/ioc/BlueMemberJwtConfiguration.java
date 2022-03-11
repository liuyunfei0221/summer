package com.blue.auth.api.ioc;

import com.blue.jwt.api.conf.BaseJwtConfParams;
import com.blue.jwt.common.JwtProcessor;
import com.blue.auth.api.conf.auth.MemberJwtConf;
import com.blue.auth.api.model.MemberPayload;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.blue.jwt.api.generator.BlueJwtProcessorGenerator.generate;
import static reactor.util.Loggers.getLogger;

/**
 * member jwt configuration
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@ConditionalOnBean(value = {MemberJwtConf.class})
@Configuration
public class BlueMemberJwtConfiguration {

    private static final Logger LOGGER = getLogger(BlueMemberJwtConfiguration.class);

    @Bean
    public JwtProcessor<MemberPayload> jwtProcessor(MemberJwtConf memberJwtConf) {
        LOGGER.info("JwtProcessor<MemberPayload> create(MemberJwtConf memberJwtConf), memberJwtConf = {}", memberJwtConf);

        BaseJwtConfParams<MemberPayload> baseJwtConfParams = new BaseJwtConfParams<>(memberJwtConf.getGlobalMaxExpireMillis(),
                memberJwtConf.getGlobalMinExpireMillis(),
                memberJwtConf.getSignKey(),
                memberJwtConf.getGammaSecrets(),
                memberJwtConf.getIssuer(),
                memberJwtConf.getSubject(),
                memberJwtConf.getAudience()) {

            @Override
            public Function<MemberPayload, Map<String, String>> getDataToClaimProcessor() {
                return p -> {
                    Map<String, String> claims = new HashMap<>(8, 2.0f);
                    claims.put("t", p.getGamma());
                    claims.put("h", p.getKeyId());
                    claims.put("i", p.getId());
                    claims.put("n", p.getLoginType());
                    claims.put("g", p.getDeviceType());
                    claims.put("s", p.getLoginTime());
                    return claims;
                };
            }

            @Override
            public Function<Map<String, String>, MemberPayload> getClaimToDataProcessor() {
                return c -> new MemberPayload(c.get("t"), c.get("h"), c.get("i"), c.get("n"), c.get("g"), c.get("s"));
            }
        };

        return generate(baseJwtConfParams);
    }

}
