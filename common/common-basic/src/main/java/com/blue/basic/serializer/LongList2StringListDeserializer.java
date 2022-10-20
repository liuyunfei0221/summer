package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotNull;

/**
 * string2long
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unchecked", "unused"})
public final class LongList2StringListDeserializer extends JsonDeserializer<List<Long>> {

    private static final LongListTypeReference REFERENCE = new LongListTypeReference();

    @Override
    public List<Long> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Object value = jsonParser.readValueAs(REFERENCE);
        if (isNotNull(value))
            return (List<Long>) value;

        return null;
    }

    private static class LongListTypeReference extends TypeReference<List<Long>> {
    }

}
