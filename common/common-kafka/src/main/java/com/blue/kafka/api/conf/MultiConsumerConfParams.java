package com.blue.kafka.api.conf;

import java.util.Map;

/**
 * 多consumer配置信息封装
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("unused")
public class MultiConsumerConfParams {

    protected Map<String, ConsumerParams> configs;

    public MultiConsumerConfParams() {
    }

    public MultiConsumerConfParams(Map<String, ConsumerParams> configs) {
        this.configs = configs;
    }

    public Map<String, ConsumerParams> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, ConsumerParams> configs) {
        this.configs = configs;
    }

    public ConsumerParams getByKey(String key) {
        return configs.get(key);
    }

    @Override
    public String toString() {
        return "MultiConsumerConfParams{" +
                "configs=" + configs +
                '}';
    }

}
