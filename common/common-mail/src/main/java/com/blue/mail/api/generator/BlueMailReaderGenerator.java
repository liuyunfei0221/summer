package com.blue.mail.api.generator;

import com.blue.mail.api.conf.MailReaderConf;
import com.blue.mail.common.MailReader;

/**
 * mail sender generator
 *
 * @author liuyunfei
 */
public final class BlueMailReaderGenerator {

    public static MailReader generateMailReader(MailReaderConf mailReaderConf) {
        return new MailReader(mailReaderConf);
    }

}
