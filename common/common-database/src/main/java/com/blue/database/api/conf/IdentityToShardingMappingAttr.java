package com.blue.database.api.conf;

/**
 * @author liuyunfei
 */
public final class IdentityToShardingMappingAttr {

    /**
     * data center/worker id
     */
    private transient Integer id;

    /**
     * database/table index
     */
    private transient Integer index;

    public IdentityToShardingMappingAttr() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "IdentityToShardingMappingAttr{" +
                "id=" + id +
                ", index=" + index +
                '}';
    }

}
