package com.blue.media.api.model;

import java.io.Serializable;

/**
 * attachment upload info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentUploadInfo implements Serializable {

    private static final long serialVersionUID = 9168850532346459216L;

    private Long id;

    private String name;

    private Long size;

    public AttachmentUploadInfo() {
    }

    public AttachmentUploadInfo(Long id, String name, Long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }

}
