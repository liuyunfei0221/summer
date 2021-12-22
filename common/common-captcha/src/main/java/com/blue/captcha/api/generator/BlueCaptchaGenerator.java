package com.blue.captcha.api.generator;

import com.blue.captcha.api.conf.CaptchaConf;
import com.blue.captcha.api.conf.CaptchaConfParams;
import com.blue.captcha.common.CaptchaProcessor;

/**
 * @author liuyunfei
 * @date 2021/12/22
 * @apiNote
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
