package com.blue.base.component.exception.common;

import org.apache.commons.lang3.StringUtils;
import reactor.util.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.FileProcessor.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.loadProp;
import static com.blue.base.common.base.PropertiesProcessor.parseProp;
import static java.util.stream.Collectors.toMap;
import static reactor.util.Loggers.getLogger;

/**
 * message processor
 *
 * @author liuyunfei
 * @date 2021/12/10
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public final class MessagesReader {

    private static final Logger LOGGER = getLogger(MessagesReader.class);

    private static final UnaryOperator<String> PRE_NAME_PARSER = n -> {
        int idx = StringUtils.lastIndexOf(n, ".");
        return idx >= 0 ? (idx > 0 ? StringUtils.substring(n, 0, idx) : "") : n;
    };

    /**
     * convert props to maps from uri
     *
     * @param uri
     * @return
     */
    public static Map<String, Map<String, String>> loadMessages(String uri) {
        List<File> files = getFiles(uri, true);

        return files.stream()
                .collect(toMap(f -> PRE_NAME_PARSER.apply(f.getName()).toLowerCase(), f -> parseProp(loadProp(f)), (a, b) -> a));
    }

}
