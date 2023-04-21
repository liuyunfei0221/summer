package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;

/**
 * double2string
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DoubleArray2StringArraySerializer extends JsonSerializer<Double[]> {

    @Override
    public void serialize(Double[] value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (isNotEmpty(value))
            jsonGenerator.writeObject(Stream.of(value).map(String::valueOf).toArray(String[]::new));
    }

}
