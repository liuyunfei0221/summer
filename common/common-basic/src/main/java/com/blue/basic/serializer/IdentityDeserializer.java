package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static com.blue.basic.common.base.BlueChecker.isNotNull;

/**
 * id string2long
 *
 * @author liuyunfei
 */
public final class IdentityDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();

        if (isNotNull(value))
            return Long.parseLong(value);

        return null;
    }

}
