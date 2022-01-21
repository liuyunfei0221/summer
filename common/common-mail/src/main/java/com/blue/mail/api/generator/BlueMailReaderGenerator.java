package com.blue.mail.api.generator;

import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.common.MailSender;

/**
 * mail sender generator
 *
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
public final class BlueMailReaderGenerator {

    public static MailSender generateMailSender(MailSenderConf mailSenderConf) {
        return new MailSender(mailSenderConf);
    }

}
