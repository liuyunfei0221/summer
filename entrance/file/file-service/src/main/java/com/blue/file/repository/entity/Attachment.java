package com.blue.file.repository.entity;


/**
 * 附件表
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class Attachment {

    /**
     * 主键
     */
    private Long id;

    /**
     * 资源链接
     */
    private String link;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 资源大小
     */
    private Long size;

    /**
     * 资源状态 0停用 1启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
    private Long creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

}