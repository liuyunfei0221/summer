package com.blue.database.api.conf;

/**
 * @author liuyunfei
 * @date 2021/11/26
 * @apiNote
 */
public final class IdentityToShardingMappingAttr {

    /**
     * data center/worker id
     */
    private Integer id;

    /**
     * database/table index
     */
    private Integer index;

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
