package com.blue.basic.common.base;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;
import static java.util.stream.Stream.of;

/**
 * XSS asserter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "RegExpRedundantEscape"})
public final class XssAsserter {

    private static final Pattern[] PATTERNS = new Pattern[]{

            compile("<script>(.*?)</script>", CASE_INSENSITIVE),

            compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", CASE_INSENSITIVE | MULTILINE | DOTALL),

            compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", CASE_INSENSITIVE | MULTILINE | DOTALL),

            compile("</script>", CASE_INSENSITIVE),

            compile("<script(.*?)>", CASE_INSENSITIVE | MULTILINE | DOTALL),

            compile("eval\\((.*?)\\)", CASE_INSENSITIVE | MULTILINE | DOTALL),

            compile("expression\\((.*?)\\)", CASE_INSENSITIVE | MULTILINE | DOTALL),

            compile("javascript:", CASE_INSENSITIVE),

            compile("vbscript:", CASE_INSENSITIVE),

            compile("onload(.*?)=", CASE_INSENSITIVE | MULTILINE | DOTALL),

            compile("alert(.*?)", CASE_INSENSITIVE | MULTILINE | DOTALL),

            compile("<", MULTILINE | DOTALL),

            compile(">", MULTILINE | DOTALL),

            compile("(document|onload|eval|script|img|svg|onerror|javascript|alert)\\\\b"),

            compile("((alert|on\\w+|function\\s+\\w+)\\s*\\(\\s*(['+\\d\\w](,?\\s*['+\\d\\w]*)*)*\\s*\\))"),

            compile("(<(script|iframe|embed|frame|frameset|object|img|applet|body|html|style|layer|link|ilayer|meta|bgsound))")
    };

    /**
     * check
     *
     * @param value
     * @return
     */
    public static boolean check(String value) {
        return of(PATTERNS).parallel()
                .anyMatch(pattern -> pattern.matcher(value).find());
    }

}
