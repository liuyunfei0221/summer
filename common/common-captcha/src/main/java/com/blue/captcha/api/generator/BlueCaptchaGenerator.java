package com.blue.captcha.api.generator;

import com.blue.captcha.api.conf.CaptchaConf;
import com.blue.captcha.api.conf.CaptchaConfParams;
import com.blue.captcha.component.CaptchaProcessor;

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
