package com.blue.kafka.api.conf;

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

    protected Map<String, ProducerParams> configs;

    public MultiProducerConfParams() {
    }

    public MultiProducerConfParams(Map<String, ProducerParams> configs) {
        this.configs = configs;
    }

    public Map<String, ProducerParams> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, ProducerParams> configs) {
        this.configs = configs;
    }

    public ProducerParams getByKey(String key) {
        return configs.get(key);
    }

    @Override
    public String toString() {
        return "MultiProducerConfParams{" +
                "configs=" + configs +
                '}';
    }

}
