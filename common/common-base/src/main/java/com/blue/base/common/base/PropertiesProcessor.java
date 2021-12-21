package com.blue.base.common.base;

import reactor.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.blue.base.common.base.FileProcessor.getFiles;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.util.Loggers.getLogger;

/**
 * prop processor
 *
 * @author liuyunfei
 * @date 2021/12/10
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class PropertiesProcessor {

    private static final Logger LOGGER = getLogger(PropertiesProcessor.class);

    /**
     * load prop
     *
     * @param file
     * @return
     */
    public static Properties loadProp(File file) {
        Properties prop = new Properties();

        if (file != null && file.isFile() && file.canRead()) {
            try (InputStream inputStream = new FileInputStream(file)) {
                prop.load(inputStream);
            } catch (IOException e) {
                LOGGER.error("Properties loadProp(File file) failed, e = {0}", e);
            }
        }

        return prop;
    }

    /**
     * convert prop to map
     *
     * @param prop
     * @return
     */
    public static Map<String, String> parseProp(Properties prop) {
        return ofNullable(prop)
                .map(p -> p.entrySet().stream().collect(toMap(e -> e.getKey().toString(), e -> e.getValue().toString(), (a, b) -> a)))
                .orElse(emptyMap());
    }

    /**
     * convert prop to map
     *
     * @param file
     * @return
     */
    public static Map<String, String> parseProp(File file) {
        return ofNullable(file)
                .filter(File::isFile)
                .map(PropertiesProcessor::loadProp)
                .map(PropertiesProcessor::parseProp)
                .orElse(emptyMap());
    }

    /**
     * find props by uri
     *
     * @param uri
     * @param recursive
     * @return
     */
    public static List<Properties> getProps(String uri, boolean recursive) {
        return getFiles(uri, recursive).stream().map(PropertiesProcessor::loadProp).collect(toList());
    }

}
