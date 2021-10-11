package com.blue.member.api.model;

import java.io.Serializable;


/**
 * member info
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MemberInfo implements Serializable {

    private static final long serialVersionUID = 7329328482721395303L;

    private Long id;

    private String name;

    private String icon;

    /**
     * @see com.blue.base.constant.member.Gender
     */
    private Integer gender;

    public MemberInfo(Long id, String name, String icon, Integer gender) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.gender = gender;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "MemberVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", gender=" + gender +
                '}';
    }
}
