package com.blue.rocket.api.conf;

import java.util.Map;

/**
 * multi consumer params
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("unused")
public class MultiConsumerConfParams {

    private Map<String, ConsumerConfParams> configs;

    public MultiConsumerConfParams() {
    }

    public Map<String, ConsumerConfParams> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, ConsumerConfParams> configs) {
        this.configs = configs;
    }

    public ConsumerConfParams getByKey(String key) {
        return configs.get(key);
    }

    @Override
    public String toString() {
        return "MultiConsumerConfParams{" +
                "configs=" + configs +
                '}';
    }

}
