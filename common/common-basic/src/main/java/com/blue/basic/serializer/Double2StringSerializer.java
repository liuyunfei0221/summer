package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static com.blue.basic.common.base.BlueChecker.isNotNull;

/**
 * double2string
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Double2StringSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (isNotNull(value))
            jsonGenerator.writeString(String.valueOf(value));
    }

}
