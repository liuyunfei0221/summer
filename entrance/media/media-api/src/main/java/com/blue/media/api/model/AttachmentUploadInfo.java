package com.blue.media.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * attachment upload info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentUploadInfo implements Serializable {

    private static final long serialVersionUID = 9168850532346459216L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long id;

    /**
     * attachment type
     *
     * @see com.blue.basic.constant.media.AttachmentType
     */
    private Integer type;

    private String name;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long size;

    public AttachmentUploadInfo() {
    }

    public AttachmentUploadInfo(Long id, Integer type, String name, Long size) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "AttachmentUploadInfo{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }

}
