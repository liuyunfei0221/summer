package com.blue.marketing.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import com.blue.marketing.repository.mapper.SignRewardTodayRelationMapper;
import com.blue.marketing.service.inter.SignRewardTodayRelationService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * sign reward today relation service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class SignRewardTodayRelationServiceImpl implements SignRewardTodayRelationService {

    private static final Logger LOGGER = getLogger(SignRewardTodayRelationServiceImpl.class);

    private final SignRewardTodayRelationMapper signRewardTodayRelationMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SignRewardTodayRelationServiceImpl(SignRewardTodayRelationMapper signRewardTodayRelationMapper) {
        this.signRewardTodayRelationMapper = signRewardTodayRelationMapper;
    }

    /**
     * select reward-date-relation by date
     *
     * @param year
     * @param month
     * @return
     */
    @Override
    public List<SignRewardTodayRelation> selectRelationByYearAndMonth(Integer year, Integer month) {
        LOGGER.info("List<SignRewardTodayRelation> selectRelationByYearAndMonth(Integer year, Integer month), year = {}, month = {}", year, month);
        if (isNull(year) || isNull(month) || year < 1 || month < 1)
            throw new BlueException(BAD_REQUEST);

        return signRewardTodayRelationMapper.selectByYearAndMonth(year, month);
    }

}
