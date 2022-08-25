package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotNull;

/**
 * ids string2long
 *
 * @author liuyunfei
 */
@SuppressWarnings("unchecked")
public final class IdentitiesDeserializer extends JsonDeserializer<List<Long>> {

    private static final ListTypeReference REFERENCE = new ListTypeReference();

    @Override
    public List<Long> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Object value = jsonParser.readValueAs(REFERENCE);
        if (isNotNull(value))
            return (List<Long>) value;

        return null;
    }

    private static class ListTypeReference extends TypeReference<List<Long>> {
        private ListTypeReference() {
        }
    }

}
