package com.blue.mail.api.generator;

import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.common.MailSender;

/**
 * mail sender generator
 *
 * @author liuyunfei
 */
public final class BlueMailSenderGenerator {

    public static MailSender generateMailSender(MailSenderConf mailSenderConf) {
        return new MailSender(mailSenderConf);
    }

}
