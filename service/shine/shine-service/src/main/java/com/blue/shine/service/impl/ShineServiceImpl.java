package com.blue.shine.service.impl;

import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.repository.template.ShineRepository;
import com.blue.shine.service.inter.ShineService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * 公益功能实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class ShineServiceImpl implements ShineService {

    private static final Logger LOGGER = getLogger(ShineServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ShineRepository shineRepository;

    public ShineServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ShineRepository shineRepository) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.shineRepository = shineRepository;
    }


    /**
     * 获取公益信息
     *
     * @param ip
     * @return
     */
    @Override
    public Mono<List<ShineInfo>> getShineInfo(String ip) {

        LOGGER.warn("ip = {}", ip);

        return shineRepository.findAll()
                .take(3)
                .collectList()
                .flatMap(shines ->
                        just(shines.stream()
                                .map(shine -> new ShineInfo(shine.getId(), shine.getTitle(), shine.getContent(), shine.getOrder(), shine.getCreateTime()
                                )).collect(toList())));
    }

}
