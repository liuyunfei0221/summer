package com.blue.base.component.exception.common;

import java.util.Map;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.FileProcessor.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.loadProp;
import static com.blue.base.common.base.PropertiesProcessor.parseProp;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;

/**
 * message processor
 *
 * @author liuyunfei
 * @date 2021/12/10
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public final class MessagesReader {

    private static final UnaryOperator<String> PRE_NAME_PARSER = n -> {
        int idx = lastIndexOf(n, SCHEME_SEPARATOR.identity);
        return idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : "") : n;
    };

    /**
     * convert props to maps from uri
     *
     * @param uri
     * @return
     */
    public static Map<String, Map<String, String>> loadMessages(String uri) {
        return getFiles(uri, true).stream()
                .collect(toMap(f -> PRE_NAME_PARSER.apply(f.getName()).toLowerCase(), f -> parseProp(loadProp(f)), (a, b) -> a));
    }

}
