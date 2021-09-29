package com.blue.pulsar.api.conf;

import java.util.Map;

/**
 * multi producer params
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("unused")
public class MultiProducerConfParams {

    protected Map<String, ProducerConfParams> configs;

    public MultiProducerConfParams() {
    }

    public MultiProducerConfParams(Map<String, ProducerConfParams> configs) {
        this.configs = configs;
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
