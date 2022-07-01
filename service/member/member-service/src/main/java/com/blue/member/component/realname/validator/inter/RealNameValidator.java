package com.blue.member.component.realname.validator.inter;

import com.blue.base.constant.member.RealNameValidatorType;
import com.blue.member.api.model.RealNameValidateResult;
import com.blue.member.repository.entity.RealName;

/**
 * real name validator inter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public interface RealNameValidator {

    /**
     * validate real name
     *
     * @param realName
     * @return
     */
    RealNameValidateResult validate(RealName realName);

    /**
     * validator type
     *
     * @return
     */
    RealNameValidatorType validatorType();

}
