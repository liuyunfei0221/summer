package com.blue.basic.common.base;

import com.blue.basic.model.exps.BlueException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import reactor.util.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.ResourceUtils.getURL;
import static reactor.util.Loggers.getLogger;

/**
 * file getter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class FileGetter {

    private static final Logger LOGGER = getLogger(FileGetter.class);

    private static final PathMatchingResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final String MATCH_ALL_PATH = "/**/*";
    private static final String MATCH_ALL_PREFIX = ".*";

    /**
     * find all files
     *
     * @param files
     * @param file
     * @param recursive
     * @return
     */
    private static List<File> listFile(List<File> files, File file, boolean recursive) {
        if (isNull(files))
            files = new LinkedList<>();

        if (isNull(file))
            return files;

        if (file.isDirectory() && recursive)
            for (File f : ofNullable(file.listFiles()).orElseGet(() -> new File[0]))
                listFile(files, f, true);

        if (file.isFile())
            files.add(file);

        return files;
    }

    /**
     * find files by uri
     *
     * @param location
     * @param recursive
     * @return
     */
    public static List<File> getFiles(String location, boolean recursive) {
        File file;
        try {
            file = new File(getURL(location).getPath());
        } catch (Exception e) {
            LOGGER.error("List<File> getFiles(String pathDir) failed, location = {}, recursive = {}, e = {}", location, recursive, e);
            return emptyList();
        }

        return listFile(new LinkedList<>(), file, recursive);
    }

    /**
     * find files by uri
     *
     * @param location
     * @return
     */
    public static List<File> getFiles(String location) {
        File file;
        try {
            file = new File(getURL(location).getPath());
        } catch (Exception e) {
            LOGGER.error("List<File> getFiles(String pathDir) failed, location = {}, e = {0}", location, e);
            return emptyList();
        }

        return listFile(new LinkedList<>(), file, true);
    }

    /**
     * find file by uri
     *
     * @param location
     * @return
     */
    public static File getFile(String location) {
        try {
            return new File(getURL(location).getPath());
        } catch (Exception e) {
            LOGGER.error("File getFile(String uri) failed, location = {}, e = {}", location, e);
            throw new BlueException(BAD_REQUEST);
        }
    }

    /**
     * get resources by class path and prefix
     *
     * @param location
     * @param prefix
     * @return
     */
    public static List<Resource> getResources(String location, String prefix) {
        try {
            return Stream.of(RESOURCE_PATTERN_RESOLVER.getResources(location + MATCH_ALL_PATH + prefix)).collect(toList());
        } catch (Exception e) {
            LOGGER.error("List<Resource> getResources(String path, String prefix) failed, location = {}, prefix = {}, e = {}", location, prefix, e);
            throw new BlueException(BAD_REQUEST);
        }
    }

    /**
     * get resources by class path
     *
     * @param location
     * @return
     */
    public static List<Resource> getResources(String location) {
        return getResources(location, MATCH_ALL_PREFIX);
    }

    /**
     * get resource by class path
     *
     * @param location
     * @return
     */
    public static Resource getResource(String location) {
        return RESOURCE_PATTERN_RESOLVER.getResource(location);
    }

}
