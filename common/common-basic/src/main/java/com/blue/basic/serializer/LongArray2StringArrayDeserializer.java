package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static com.blue.basic.common.base.BlueChecker.isNotNull;

/**
 * string2long
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class LongArray2StringArrayDeserializer extends JsonDeserializer<Long[]> {

    private static final LongArrayTypeReference REFERENCE = new LongArrayTypeReference();

    @Override
    public Long[] deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Object value = jsonParser.readValueAs(REFERENCE);
        if (isNotNull(value))
            return (Long[]) value;

        return null;
    }

    private static class LongArrayTypeReference extends TypeReference<Long[]> {
    }

}
