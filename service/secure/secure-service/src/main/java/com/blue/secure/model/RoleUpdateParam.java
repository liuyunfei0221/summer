package com.blue.secure.model;

/**
 * params for update a exist role
 *
 * @author liuyunfei
 * @date 2021/11/3
 * @apiNote
 */
@SuppressWarnings("unused")
public class RoleUpdateParam extends RoleInsertParam {

    private static final long serialVersionUID = 3709726547884800171L;

    private Long id;

    public RoleUpdateParam() {
    }

    public RoleUpdateParam(String name, String description, Long id) {
        super(name, description);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RoleUpdateParam{" +
                "id=" + id +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                '}';
    }
}
