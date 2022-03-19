package com.blue.es.api.conf;

/**
 * default header
 *
 * @author liuyunfei
 * @date 2021/9/20
 * @apiNote
 */
@SuppressWarnings("unused")
public final class DefaultHeader {

    private transient String name;

    private transient String value;

    public DefaultHeader() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DefaultHeader{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
