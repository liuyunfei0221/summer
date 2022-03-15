package com.blue.verify.api.generator;

import com.blue.verify.api.conf.CaptchaConf;
import com.blue.verify.api.conf.CaptchaConfParams;
import com.blue.verify.common.CaptchaProcessor;

/**
 * captcha processor generator
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class BlueCaptchaGenerator {

    private static final CaptchaConf DEFAULT_CONF = new CaptchaConfParams();

    public static CaptchaProcessor generateCaptchaProcessor() {
        return generateCaptchaProcessor(DEFAULT_CONF);
    }

    public static CaptchaProcessor generateCaptchaProcessor(CaptchaConf captchaConf) {
        return new CaptchaProcessor(captchaConf != null ? captchaConf : DEFAULT_CONF);
    }

}
