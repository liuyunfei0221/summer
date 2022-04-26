package com.blue.portal.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.portal.api.model.BulletinManagerInfo;
import com.blue.portal.constant.BulletinSortAttribute;
import com.blue.portal.model.BulletinCondition;
import com.blue.portal.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.repository.mapper.BulletinMapper;
import com.blue.portal.service.inter.BulletinService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConditionSortProcessor.process;
import static com.blue.base.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.portal.converter.PortalModelConverters.bulletinToBulletinManagerInfo;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;

/**
 * bulletin service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class BulletinServiceImpl implements BulletinService {

    private static final Logger LOGGER = Loggers.getLogger(BulletinServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BulletinMapper bulletinMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BulletinServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BulletinMapper bulletinMapper) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.bulletinMapper = bulletinMapper;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(BulletinSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<BulletinCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new BulletinCondition();

        process(condition, SORT_ATTRIBUTE_MAPPING, BulletinSortAttribute.ID.column);

        ofNullable(condition.getTitleLike())
                .filter(StringUtils::hasText).ifPresent(titleLike -> condition.setTitleLike("%" + titleLike + "%"));
        ofNullable(condition.getLinkLike())
                .filter(StringUtils::hasText).ifPresent(linkLike -> condition.setLinkLike("%" + linkLike + "%"));

        return condition;
    };

    private static final Function<List<Bulletin>, List<Long>> OPERATORS_GETTER = bulletins -> {
        Set<Long> operatorIds = new HashSet<>(bulletins.size());

        for (Bulletin b : bulletins) {
            operatorIds.add(b.getCreator());
            operatorIds.add(b.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    /**
     * insert bulletin
     *
     * @param bulletin
     * @return
     */
    @Override
    public int insert(Bulletin bulletin) {
        LOGGER.info("int insert(Bulletin bulletin), bulletin = {}", bulletin);
        return ofNullable(bulletin)
                .map(bulletinMapper::insert)
                .orElse(0);
    }

    /**
     * get bulletin by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Bulletin> getBulletin(Long id) {
        LOGGER.info("Optional<Bulletin> getBulletin(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(bulletinMapper.selectByPrimaryKey(id));
    }

    /**
     * get attachment mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Bulletin>> getBulletinMono(Long id) {
        return just(this.getBulletin(id));
    }

    /**
     * select all bulletins
     *
     * @return
     */
    @Override
    public Mono<List<Bulletin>> selectBulletin() {
        LOGGER.info("Mono<List<Bulletin>> selectBulletin()");
        return just(bulletinMapper.select());
    }

    /**
     * list active bulletins by type
     *
     * @param bulletinType
     * @return
     */
    @Override
    public List<Bulletin> selectAllPortalBulletinByType(Integer bulletinType) {
        LOGGER.info("List<Bulletin> selectTargetActiveBulletinByType(Integer bulletinType), bulletinType = {}", bulletinType);
        assertBulletinType(bulletinType, false);

        List<Bulletin> bulletins = bulletinMapper.selectAllPortalBulletinByCondition(TIME_STAMP_GETTER.get(), bulletinType, Status.VALID.status);
        LOGGER.info("List<Bulletin> selectActiveBulletinByType(BulletinType bulletinType), bulletins = {}", bulletins);

        return bulletins;
    }

    /**
     * select bulletin by page and condition
     *
     * @param limit
     * @param rows
     * @param bulletinCondition
     * @return
     */
    @Override
    public Mono<List<Bulletin>> selectBulletinMonoByLimitAndCondition(Long limit, Long rows, BulletinCondition bulletinCondition) {
        LOGGER.info("Mono<List<Bulletin>> selectBulletinMonoByLimitAndCondition(Long limit, Long rows, BulletinCondition bulletinCondition), " +
                "limit = {}, rows = {}, bulletinCondition = {}", limit, rows, bulletinCondition);
        return just(bulletinMapper.selectByLimitAndCondition(limit, rows, bulletinCondition));
    }

    /**
     * count bulletin by condition
     *
     * @param bulletinCondition
     * @return
     */
    @Override
    public Mono<Long> countBulletinMonoByCondition(BulletinCondition bulletinCondition) {
        LOGGER.info("Mono<Long> countBulletinMonoByCondition(BulletinCondition bulletinCondition), bulletinCondition = {}", bulletinCondition);
        return just(ofNullable(bulletinMapper.countByCondition(bulletinCondition)).orElse(0L));
    }

    /**
     * select bulletin info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<BulletinManagerInfo>> selectBulletinInfoPageMonoByPageAndCondition(PageModelRequest<BulletinCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<BulletinManagerInfo>> selectBulletinInfoPageMonoByPageAndCondition(PageModelRequest<BulletinCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        BulletinCondition bulletinCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectBulletinMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), bulletinCondition),
                countBulletinMonoByCondition(bulletinCondition))
                .flatMap(tuple2 -> {
                    List<Bulletin> bulletins = tuple2.getT1();
                    return isNotEmpty(bulletins) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(OPERATORS_GETTER.apply(bulletins))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(bulletins.stream().map(b ->
                                                bulletinToBulletinManagerInfo(b, ofNullable(idAndNameMapping.get(b.getCreator())).orElse(""),
                                                        ofNullable(idAndNameMapping.get(b.getUpdater())).orElse(""))).collect(toList()));
                                    }).flatMap(bulletinManagerInfos ->
                                            just(new PageModelResponse<>(bulletinManagerInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
