package com.blue.media.config.blue;

import com.blue.mail.api.conf.MailConfParams;
import com.sanctionco.jmail.EmailValidator;
import com.sanctionco.jmail.JMail;
import com.sanctionco.jmail.TopLevelDomain;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "mail")
public class BlueMailConfig extends MailConfParams {

    @Override
    public EmailValidator getEmailValidator() {
        return JMail.strictValidator()
                .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM)
                .withRule(email -> true);
    }

}
