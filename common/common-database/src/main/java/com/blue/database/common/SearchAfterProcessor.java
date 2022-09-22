package com.blue.database.common;


import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;

/**
 * search after processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public final class SearchAfterProcessor {

    /**
     * parse data and search after
     *
     * @param data
     * @param searchAfterParser
     * @param <T>
     * @param <A>
     * @return
     */
    public static <T extends Serializable, A extends Serializable> A parseSearchAfter(List<T> data, Function<T, A> searchAfterParser) {
        if (isEmpty(data) || isNull(searchAfterParser))
            return null;

        int index = data.size() - 1;

        T t;
        A searchAfter;
        for (int i = 0; index >= i; index--)
            if (isNotNull(t = data.get(index)) && isNotNull(searchAfter = searchAfterParser.apply(t)))
                return searchAfter;

        return null;
    }

}
