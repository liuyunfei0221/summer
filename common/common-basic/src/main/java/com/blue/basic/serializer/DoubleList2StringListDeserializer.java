package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

/**
 * string2double
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class DoubleList2StringListDeserializer extends JsonDeserializer<List<Double>> {

    private static final DoubleListTypeReference REFERENCE = new DoubleListTypeReference();

    @Override
    public List<Double> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.readValueAs(REFERENCE);
    }

    private static class DoubleListTypeReference extends TypeReference<List<Double>> {
    }

}
