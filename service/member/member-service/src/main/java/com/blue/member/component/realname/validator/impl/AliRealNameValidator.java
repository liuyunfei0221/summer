package com.blue.member.component.realname.validator.impl;

import com.blue.basic.constant.member.RealNameValidatorType;
import com.blue.member.api.model.RealNameValidateResult;
import com.blue.member.component.realname.validator.inter.RealNameValidator;
import com.blue.member.repository.entity.RealName;

import static com.blue.basic.constant.member.RealNameValidatorType.ALI;

/**
 * real name validator inter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class AliRealNameValidator implements RealNameValidator {

    /**
     * validate real name
     *
     * @param realName
     * @return
     */
    @Override
    public RealNameValidateResult validate(RealName realName) {

        //TODO
        return new RealNameValidateResult(true, "success");
    }

    /**
     * validator type
     *
     * @return
     */
    @Override
    public RealNameValidatorType validatorType() {
        return ALI;
    }

}
