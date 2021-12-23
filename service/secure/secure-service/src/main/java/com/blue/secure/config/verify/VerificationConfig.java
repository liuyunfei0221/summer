//package com.blue.secure.config.verify;
//
//import com.blue.secure.component.verify.VerificationCodeProcessor;
//import com.blue.secure.config.deploy.VerifyDeploy;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import reactor.util.Logger;
//
//import static reactor.util.Loggers.getLogger;
//
///**
// * verify config
// *
// * @author liuyunfei
// * @date 2021/8/18
// * @apiNote
// */
//@Configuration
//public class VerificationConfig {
//
//    private static final Logger LOGGER = getLogger(VerificationConfig.class);
//
//    private final VerifyDeploy verifyDeploy;
//
//    public VerificationConfig(VerifyDeploy verifyDeploy) {
//        this.verifyDeploy = verifyDeploy;
//    }
//
//    @Bean
//    VerificationCodeProcessor verificationCodeProcessor(StringRedisTemplate stringRedisTemplate) {
//        LOGGER.info("verifyDeploy = {}", verifyDeploy);
//        return new VerificationCodeProcessor(stringRedisTemplate, verifyDeploy.getDefaultType(), verifyDeploy.getDefaultLength(), verifyDeploy.getDefaultExpireMillis());
//    }
//
//}
