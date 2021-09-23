package com.blue.base.model.base;

import java.io.Serializable;

import static java.util.Optional.ofNullable;

/**
 * 用于POST提交的主键数据封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class IdentityWrapper implements Serializable {

    private static final long serialVersionUID = 3631693809321831634L;

    /**
     * 数据主键
     */
    private Long id;

    public IdentityWrapper() {
    }

    public IdentityWrapper(Long id) {
        this.id = id;
    }

    public Long getId() {
        return ofNullable(id).orElse(0L);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IdentityDTO{" +
                "id=" + id +
                '}';
    }
}
