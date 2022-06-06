package com.blue.member.model;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;

/**
 * params for update an address
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class MemberAddressUpdateParam extends MemberAddressInsertParam {

    private static final long serialVersionUID = -1263578787336267342L;

    private Long id;

    public MemberAddressUpdateParam() {
    }

    public MemberAddressUpdateParam(Long id, String memberName, Integer gender, String phone, String email, Long cityId, Long areaId, String address, String reference, String extra) {
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
        return "MemberAddressUpdateParam{" +
                "id=" + id +
                ", memberName='" + super.getMemberName() + '\'' +
                ", gender=" + super.getGender() +
                ", phone='" + super.getPhone() + '\'' +
                ", email='" + super.getEmail() + '\'' +
                ", cityId=" + super.getCityId() +
                ", areaId=" + super.getAreaId() +
                ", address='" + super.getAddress() + '\'' +
                ", reference='" + super.getReference() + '\'' +
                ", extra='" + super.getExtra() + '\'' +
                '}';
    }

}
