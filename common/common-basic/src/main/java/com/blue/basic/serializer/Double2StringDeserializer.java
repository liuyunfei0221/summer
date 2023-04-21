package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static java.util.Optional.ofNullable;

/**
 * string2double
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Double2StringDeserializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return ofNullable(jsonParser.getText())
                .map(Double::parseDouble)
                .orElse(null);
    }

}
