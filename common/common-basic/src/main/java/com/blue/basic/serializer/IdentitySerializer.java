package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static com.blue.basic.common.base.BlueChecker.isNotNull;

/**
 * id long转string
 *
 * @author liuyunfei
 */
public final class IdentitySerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (isNotNull(value))
            jsonGenerator.writeString(String.valueOf(value));
    }

}
