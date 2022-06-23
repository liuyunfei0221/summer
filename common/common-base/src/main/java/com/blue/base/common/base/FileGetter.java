package com.blue.base.common.base;

import com.blue.base.model.exps.BlueException;
import reactor.util.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
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
     * @param uri
     * @param recursive
     * @return
     */
    public static List<File> getFiles(String uri, boolean recursive) {
        File file;
        try {
            file = new File(getURL(uri).getPath());
        } catch (Exception e) {
            LOGGER.error("List<File> getFiles(String pathDir) failed, uri = {}, recursive = {}, e = {0}", uri, recursive, e);
            return emptyList();
        }

        return listFile(new LinkedList<>(), file, recursive);
    }

    /**
     * find file by uri
     *
     * @param uri
     * @return
     */
    public static File getFile(String uri) {
        try {
            return new File(getURL(uri).getPath());
        } catch (Exception e) {
            LOGGER.error("File getFile(String uri) failed, uri = {}, e = {0}", uri, e);
            throw new BlueException(BAD_REQUEST);
        }
    }

}
