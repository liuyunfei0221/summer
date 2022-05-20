package com.blue.media.common.part;

import com.blue.media.common.part.inter.PartInfoHandler;
import org.springframework.http.codec.multipart.Part;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.ClassGetter.getClassesByPackage;
import static com.blue.media.common.part.constant.FilePartElementKey.PART_CLASS;
import static com.blue.media.common.part.constant.FilePartElementKey.PART_NAME;
import static java.util.stream.Collectors.toMap;
import static reactor.util.Loggers.getLogger;

/**
 * media info processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SameParameterValue", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class PartInfoProcessor {

    private static final Logger LOGGER = getLogger(PartInfoProcessor.class);

    /**
     * implements package
     */
    private static final String DIR_NAME = "com.blue.media.common.part.impl";

    private static final Map<String, PartInfoHandler> MAPPING = generatorMapping(DIR_NAME);

    /**
     * init handler mapping
     *
     * @param dirName
     * @return
     */
    private static Map<String, PartInfoHandler> generatorMapping(String dirName) {
        List<Class<?>> classes = getClassesByPackage(dirName, true);

        LOGGER.warn("load classes");
        return classes
                .stream()
                .filter(clz -> !clz.isInterface() &&
                        Stream.of(clz.getInterfaces()).anyMatch(inter -> PartInfoHandler.class.getName().equals(inter.getName()))
                )
                .map(clz -> {
                    try {
                        LOGGER.warn("load clz->" + clz.getName());
                        return (PartInfoHandler) clz.getConstructor().newInstance();
                    } catch (Exception e) {
                        LOGGER.error("newInstance() FAILED, e = ", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(pih ->
                        pih.classesCanHandle()
                                .stream()
                                .collect(toMap(k -> k, v -> pih, (a, b) -> a))
                ).flatMap(m ->
                        m.entrySet().stream()
                ).collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a)
                );
    }

    /**
     * handle
     *
     * @param part
     * @return
     */
    public static Map<String, String> process(Part part) {
        LOGGER.warn("part = {}", part);

        String partClzName = part.getClass().getName();

        PartInfoHandler handler = MAPPING.get(partClzName);
        if (isNotNull(handler))
            try {
                return handler.process(part);
            } catch (Exception e) {
                LOGGER.error("parse part failed, part = {}, e = {}", part, e);
            }

        LOGGER.error("un handled part, part = {}", part);
        Map<String, String> info = new HashMap<>(4);
        info.put(PART_CLASS.identity, partClzName);
        info.put(PART_NAME.identity, part.name());

        return info;
    }

}
