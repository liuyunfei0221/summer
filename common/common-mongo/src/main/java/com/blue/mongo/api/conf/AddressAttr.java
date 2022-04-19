package com.blue.mongo.api.conf;

/**
 * mongo server conf
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AddressAttr {

    private transient String address;

    private transient Integer port;

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
