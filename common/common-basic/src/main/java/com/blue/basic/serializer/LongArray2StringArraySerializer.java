package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;

/**
 * long2string
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class LongArray2StringArraySerializer extends JsonSerializer<Long[]> {

    @Override
    public void serialize(Long[] value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (isNotEmpty(value))
            jsonGenerator.writeObject(Stream.of(value).map(String::valueOf).toArray(String[]::new));
    }

}
