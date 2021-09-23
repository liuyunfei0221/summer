package com.blue.mongo.api.conf;

/**
 * @author liuyunfei
 * @date 2021/9/15
 * @apiNote
 */
public final class AddressAttr {

    private String address;

    private Integer port;

    public AddressAttr() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "MongoAddressAttr{" +
                "address='" + address + '\'' +
                ", port=" + port +
                '}';
    }

}
