package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static java.lang.Long.parseLong;

/**
 * string2long
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Long2StringDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();

        return isNotBlank(value) ? parseLong(value) : null;
    }

}
