package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

/**
 * string2long
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class LongList2StringListDeserializer extends JsonDeserializer<List<Long>> {

    private static final LongListTypeReference REFERENCE = new LongListTypeReference();

    @Override
    public List<Long> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.readValueAs(REFERENCE);
    }

    private static class LongListTypeReference extends TypeReference<List<Long>> {
    }

}
