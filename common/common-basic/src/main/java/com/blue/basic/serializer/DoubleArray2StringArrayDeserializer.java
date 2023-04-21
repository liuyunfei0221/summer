package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * string2double
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class DoubleArray2StringArrayDeserializer extends JsonDeserializer<Double[]> {

    private static final DoubleArrayTypeReference REFERENCE = new DoubleArrayTypeReference();

    @Override
    public Double[] deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.readValueAs(REFERENCE);
    }

    private static class DoubleArrayTypeReference extends TypeReference<Double[]> {
    }

}
