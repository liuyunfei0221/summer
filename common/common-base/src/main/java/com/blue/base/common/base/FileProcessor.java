package com.blue.base.common.base;

import reactor.util.Logger;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static reactor.util.Loggers.getLogger;

/**
 * file processor
 *
 * @author liuyunfei
 * @date 2021/12/10
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class FileProcessor {

    private static final Logger LOGGER = getLogger(FileProcessor.class);

    /**
     * find all files
     *
     * @param files
     * @param file
     * @param recursive
     * @return
     */
    private static List<File> listFile(List<File> files, File file, boolean recursive) {
        if (files == null)
            files = new LinkedList<>();

        if (file == null)
            return files;

        if (file.isDirectory() && recursive)
            for (File f : ofNullable(file.listFiles()).orElse(new File[0]))
                listFile(files, f, true);

        if (file.isFile())
            files.add(file);

        return files;
    }

    /**
     * find files by uri
     *
     * @param uri
     * @param recursive
     * @return
     */
    public static List<File> getFiles(String uri, boolean recursive) {
        String path = URI.create(uri).getPath();

        if (isBlank(path))
            return emptyList();

        File file;
        try {
            file = new File(path);
        } catch (Exception e) {
            LOGGER.error("List<File> getFiles(String pathDir) failed, e = {0}", e);
            return emptyList();
        }

        return listFile(new LinkedList<>(), file, recursive);
    }

}