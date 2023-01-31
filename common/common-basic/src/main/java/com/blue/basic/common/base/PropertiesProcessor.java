package com.blue.basic.common.base;

import org.springframework.core.io.Resource;
import reactor.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.FileGetter.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.util.Loggers.getLogger;

/**
 * prop processor
 *
 * @author liuyunfei
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

        if (isNotNull(file) && file.isFile() && file.canRead()) {
            try (InputStream inputStream = new FileInputStream(file)) {
                prop.load(inputStream);
                LOGGER.info("loadProp, file = {}, prop = {}", file, prop);
            } catch (IOException e) {
                LOGGER.error("loadProp failed, file = {}, e = {}", file, e);
            }
        }

        return prop;
    }

    /**
     * load prop
     *
     * @param resource
     * @return
     */
    public static Properties loadProp(Resource resource) {
        Properties prop = new Properties();

        if (isNotNull(resource) && resource.exists()) {
            try {
                prop.load(resource.getInputStream());
                LOGGER.info("loadProp, resource = {}, prop = {}", resource, prop);
            } catch (IOException e) {
                LOGGER.error("loadProp failed, resource = {}, e = {}", resource, e);
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
                .orElseGet(Collections::emptyMap);
    }

    /**
     * convert resource to map
     *
     * @param resource
     * @return
     */
    public static Map<String, String> parseProp(Resource resource) {
        return ofNullable(resource)
                .filter(Resource::exists)
                .map(PropertiesProcessor::loadProp)
                .map(PropertiesProcessor::parseProp)
                .orElseGet(Collections::emptyMap);
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
                .orElseGet(Collections::emptyMap);
    }

    /**
     * find props by class path
     *
     * @param location
     * @param prefix
     * @return
     */
    public static List<Properties> getPropsByClassPath(String location, String prefix) {
        return getResources(location, prefix).stream().map(PropertiesProcessor::loadProp).collect(toList());
    }

    /**
     * find props by class path
     *
     * @param location
     * @return
     */
    public static List<Properties> getPropsByClassPath(String location) {
        return getResources(location).stream().map(PropertiesProcessor::loadProp).collect(toList());
    }

    /**
     * find prop by class path
     *
     * @param location
     * @return
     */
    public static Properties getPropByClassPath(String location) {
        return loadProp(getResource(location));
    }

    /**
     * find props by file
     *
     * @param location
     * @param recursive
     * @return
     */
    public static List<Properties> getPropsByFile(String location, boolean recursive) {
        return getFiles(location, recursive).stream().map(PropertiesProcessor::loadProp).collect(toList());
    }

    /**
     * find props by file
     *
     * @param location
     * @return
     */
    public static List<Properties> getPropsByFile(String location) {
        return getFiles(location).stream().map(PropertiesProcessor::loadProp).collect(toList());
    }

    /**
     * find props by file
     *
     * @param location
     * @return
     */
    public static Properties getPropByFile(String location) {
        return loadProp(getFile(location));
    }

}