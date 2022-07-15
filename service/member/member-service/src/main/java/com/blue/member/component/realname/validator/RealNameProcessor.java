package com.blue.member.component.realname.validator;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.model.RealNameValidateResult;
import com.blue.member.component.realname.validator.inter.RealNameValidator;
import com.blue.member.config.deploy.ValidatorTypeDeploy;
import com.blue.member.repository.entity.RealName;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * real name processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
@Component
public class RealNameProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(RealNameProcessor.class);

    /**
     * target validator
     */
    private RealNameValidator realNameValidator;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        Map<String, RealNameValidator> realNameValidators = applicationContext.getBeansOfType(RealNameValidator.class);
        if (isEmpty(realNameValidators))
            throw new RuntimeException("realNameValidators is empty");

        ValidatorTypeDeploy validatorTypeDeploy;
        try {
            validatorTypeDeploy = applicationContext.getBean(ValidatorTypeDeploy.class);
            LOGGER.info("RealNameProcessor onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent), validatorTypeDeploy = {}", validatorTypeDeploy);
        } catch (BeansException e) {
            LOGGER.error("applicationContext.getBean(ValidatorTypeDeploy.class), e = {}", e);
            throw new RuntimeException("applicationContext.getBean(ValidatorTypeDeploy.class) failed");
        }

        String validatorType = ofNullable(validatorTypeDeploy.getType())
                .filter(BlueChecker::isNotBlank)
                .orElseThrow(() -> new RuntimeException("validatorType is blank"));

        realNameValidator = realNameValidators.values().stream()
                .filter(rnv -> validatorType.equals(rnv.validatorType().identity))
                .findAny().orElseThrow(() -> new RuntimeException("validator with target type " + validatorType + " not found"));
    }

    /**
     * validate
     *
     * @param realName
     * @return
     */
    public RealNameValidateResult validate(RealName realName) {
        LOGGER.info("RealNameValidateResult validate(RealName realName), realName = {}", realName);
        if (isNull(realName))
            throw new BlueException(EMPTY_PARAM);

        return realNameValidator.validate(realName);
    }

}
