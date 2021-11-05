package com.blue.base.model.base;

import java.io.Serializable;

import static java.util.Optional.ofNullable;

/**
 * id param
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class IdentityParam implements Serializable {

    private static final long serialVersionUID = 3631693809321831634L;

    /**
     * id
     */
    private Long id;

    public IdentityParam() {
    }

    public IdentityParam(Long id) {
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
