package com.blue.media.config.blue;

import com.blue.mail.api.conf.MailSenderConfParams;
import com.sanctionco.jmail.EmailValidator;
import com.sanctionco.jmail.JMail;
import com.sanctionco.jmail.TopLevelDomain;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static com.sanctionco.jmail.TopLevelDomain.*;
import static java.util.stream.Collectors.toList;

/**
 * redis config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "sender")
public class BlueMailSenderConfig extends MailSenderConfParams {

    private static final TopLevelDomain[] TOP_LEVEL_DOMAINS = Stream.of(
            DOT_COM, DOT_ORG, DOT_NET, DOT_INT, DOT_EDU, DOT_GOV, DOT_MIL, TopLevelDomain.fromString("ai")
    ).collect(toList()).toArray(TopLevelDomain[]::new);

    @Override
    public EmailValidator getEmailValidator() {
        return JMail.strictValidator()
                .requireOnlyTopLevelDomains(TOP_LEVEL_DOMAINS);
    }

}
