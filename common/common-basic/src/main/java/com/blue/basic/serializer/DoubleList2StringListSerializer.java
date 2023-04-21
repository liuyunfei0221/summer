package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;
import static java.util.stream.Collectors.toList;

/**
 * double2string
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DoubleList2StringListSerializer extends JsonSerializer<List<Double>> {

    @Override
    public void serialize(List<Double> value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (isNotEmpty(value))
            jsonGenerator.writeObject(value.stream().map(String::valueOf).collect(toList()));
    }

}
