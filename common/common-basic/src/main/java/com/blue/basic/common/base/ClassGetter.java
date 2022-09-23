package com.blue.basic.common.base;

import com.blue.basic.model.exps.BlueException;
import org.springframework.util.StringUtils;
import reactor.util.Logger;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.io.File.separator;
import static java.lang.Class.forName;
import static java.lang.Thread.currentThread;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.indexOf;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * util to obtain classes from package
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class ClassGetter {

    private static final Logger LOGGER = getLogger(ClassGetter.class);

    /**
     * class suffix
     */
    private static final String CLASS_SUFFIX = ".class";

    /**
     * class prefix
     */
    private static final String JAVA_MAIN_PREFIX = separator + "java" + separator + "main" + separator;

    /**
     * target prefix
     */
    private static final String CLASS_PREFIX = separator + "classes" + separator;

    /**
     * separators
     */
    private static final String PATH_SEPARATOR = "/", PACKAGE_SEPARATOR = ".";

    /**
     * inner class token
     */
    private static final String INNER_CLASS_IDENTITY = "$";

    /**
     * protocols
     */
    private static final String FILE_PROTOCOL = "file", JAR_PROTOCOL = "jar";


    /**
     * get classes by package
     *
     * @param packageName
     * @param recursive
     * @return
     */
    public static List<Class<?>> getClassesByPackage(String packageName, boolean recursive) {
        LOGGER.info("List<Class<?>> getClassesByPackage(String packageName, boolean recursive), packageName = {}, recursive = {}", packageName, recursive);

        List<String> clzNames = new LinkedList<>();
        ClassLoader loader = currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(packageName.replaceAll("\\.", "/"));

            String protocol;
            String path;
            JarFile jarFile;

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (isNotNull(url)) {
                    protocol = url.getProtocol();
                    if (FILE_PROTOCOL.equals(protocol)) {
                        path = url.getPath();
                        clzNames.addAll(getAllClassNameByFile(new File(path), recursive));
                    } else if (JAR_PROTOCOL.equals(protocol)) {
                        jarFile = null;
                        try {
                            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        } catch (Exception e) {
                            LOGGER.error("getClassesByPackage(String packageName, boolean recursive) failed, Exception e = {}", e);
                        }
                        if (isNotNull(jarFile))
                            clzNames.addAll(getAllClassNameByJar(jarFile, packageName, recursive));
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("getClassesByPackage(String packageName, boolean recursive) failed, IOException e = {}", e);
        }

        if (isEmpty(clzNames))
            return emptyList();

        return clzNames.stream().filter(StringUtils::hasText)
                .map(cn -> {
                    try {
                        return forName(cn);
                    } catch (ClassNotFoundException e) {
                        LOGGER.error("getClassesByPackage(String packageName, boolean recursive) failed, ClassNotFoundException e = {}", e);
                    }
                    return null;
                }).filter(Objects::nonNull).collect(toList());
    }

    private static final BiConsumer<File, List<String>> HANDLE_FILE = (file, clzNames) -> {
        String path = file.getPath();
        if (path.endsWith(CLASS_SUFFIX))
            handlePath(path, clzNames);
    };

    private static List<String> getAllClassNameByFile(File file, boolean recursive) {
        List<String> clzNames = new LinkedList<>();
        if (!file.exists())
            return clzNames;

        if (file.isFile()) {
            HANDLE_FILE.accept(file, clzNames);
        } else {
            File[] listFiles = file.listFiles();
            if (isNotNull(listFiles) && listFiles.length > 0) {
                for (File f : listFiles)
                    if (recursive) {
                        clzNames.addAll(getAllClassNameByFile(f, true));
                    } else {
                        if (f.isFile()) {
                            HANDLE_FILE.accept(file, clzNames);
                        }
                    }
            }
        }
        return clzNames;
    }

    private static void handlePath(String path, List<String> clzNames) {
        String clazzName;
        if (path.contains(JAVA_MAIN_PREFIX)) {
            path = path.replace(CLASS_SUFFIX, EMPTY_VALUE.value);
            clazzName = path.substring(indexOf(path, JAVA_MAIN_PREFIX) + JAVA_MAIN_PREFIX.length())
                    .replace(separator, PACKAGE_SEPARATOR);
        } else if (path.contains(CLASS_PREFIX)) {
            path = path.replace(CLASS_SUFFIX, EMPTY_VALUE.value);
            clazzName = path.substring(indexOf(path, CLASS_PREFIX) + CLASS_PREFIX.length())
                    .replace(separator, PACKAGE_SEPARATOR);
        } else {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "handlePath(String path, List<String> clzNames) failed, path = " + path);
        }
        if (!clazzName.contains(INNER_CLASS_IDENTITY))
            clzNames.add(clazzName);
    }

    private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean recursive) {
        List<String> clzNames = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();

        JarEntry jarEntry;
        String name;
        while (entries.hasMoreElements()) {
            jarEntry = entries.nextElement();
            name = jarEntry.getName();

            if (name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, EMPTY_VALUE.value).replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);
                if (recursive) {
                    if (name.startsWith(packageName) && !name.contains(INNER_CLASS_IDENTITY))
                        clzNames.add(name);
                } else {
                    if (packageName.equals(name.substring(0, lastIndexOf(name, PACKAGE_SEPARATOR))) && !name.contains(INNER_CLASS_IDENTITY))
                        clzNames.add(name);
                }
            }
        }

        return clzNames;
    }

}
