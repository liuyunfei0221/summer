package com.blue.kafka.utils;

import com.blue.kafka.api.conf.ConsumerConf;
import com.blue.kafka.api.conf.ProducerConf;

import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;
import static org.apache.kafka.clients.admin.AdminClientConfig.SECURITY_PROTOCOL_CONFIG;
import static org.springframework.util.StringUtils.hasText;

/**
 * kafka auth util
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "DuplicatedCode"})
public final class AuthProcessor {

    private static final String JAAS_TEMPLATE = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";

    /**
     * handle auth info for producer
     *
     * @param configs
     * @param producerConf
     */
    public static void authForProducer(Map<String, Object> configs, ProducerConf producerConf) {
        String username = producerConf.getUsername();
        String password = producerConf.getPassword();

        if (hasText(username) && hasText(password)) {
            configs.put(SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            configs.put("sasl.mechanism", "SCRAM-SHA-256");
            configs.put("sasl.jaas.config", format(JAAS_TEMPLATE, username, password));
        }

    }

    /**
     * handle auth info for consumer
     *
     * @param configs
     * @param consumerConf
     */
    public static void authForConsumer(Properties configs, ConsumerConf consumerConf) {
        String username = consumerConf.getUsername();
        String password = consumerConf.getPassword();

        if (hasText(username) && hasText(password)) {
            configs.put(SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            configs.put("sasl.mechanism", "SCRAM-SHA-256");
            configs.put("sasl.jaas.config", format(JAAS_TEMPLATE, username, password));
        }
    }

}
