package com.blue.member.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update an address
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class AddressUpdateParam extends AddressInsertParam {

    private static final long serialVersionUID = -1263578787336267342L;

    private Long id;

    public AddressUpdateParam() {
    }

    public AddressUpdateParam(Long id, String memberName, Integer gender, String phone, String email, Long cityId, Long areaId, String address, String reference, String extra) {
        super(memberName, gender, phone, email, cityId, areaId, address, reference, extra);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        super.asserts();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AddressUpdateParam{" +
                "id=" + id +
                ", contact='" + super.getContact() + '\'' +
                ", gender=" + super.getGender() +
                ", phone='" + super.getPhone() + '\'' +
                ", email='" + super.getEmail() + '\'' +
                ", cityId=" + super.getCityId() +
                ", areaId=" + super.getAreaId() +
                ", detail='" + super.getDetail() + '\'' +
                ", reference='" + super.getReference() + '\'' +
                ", extra='" + super.getExtra() + '\'' +
                '}';
    }

}
