package com.blue.rocket.api.conf;

import java.util.Map;

/**
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
public class MultiProducerConfParams {

    private Map<String, ProducerConfParams> configs;

    public MultiProducerConfParams() {
    }

    public Map<String, ProducerConfParams> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, ProducerConfParams> configs) {
        this.configs = configs;
    }

    public ProducerConfParams getByKey(String key) {
        return configs.get(key);
    }

    @Override
    public String toString() {
        return "MultiProducerConfParams{" +
                "configs=" + configs +
                '}';
    }

}
