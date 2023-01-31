package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static java.util.Optional.ofNullable;

/**
 * string2long
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Long2StringDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return ofNullable(jsonParser.getText())
                .map(Long::parseLong)
                .orElse(null);
    }

}
