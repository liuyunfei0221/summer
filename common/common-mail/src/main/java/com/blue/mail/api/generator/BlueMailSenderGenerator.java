package com.blue.mail.api.generator;

import com.blue.mail.api.conf.MailConf;
import com.blue.mail.common.MailSender;

/**
 * mail sender generator
 *
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
public final class BlueMailSenderGenerator {

    public static MailSender generateMailSender(MailConf mailConf) {
        return new MailSender(mailConf);
    }

}
