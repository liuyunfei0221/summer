package com.blue.verify.api.generator;

import com.blue.verify.api.conf.CaptchaConf;
import com.blue.verify.api.conf.CaptchaConfParams;
import com.blue.verify.common.CaptchaProcessor;

import static com.blue.base.common.base.BlueChecker.isNotNull;

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
        return new CaptchaProcessor(isNotNull(captchaConf) ? captchaConf : DEFAULT_CONF);
    }

}
