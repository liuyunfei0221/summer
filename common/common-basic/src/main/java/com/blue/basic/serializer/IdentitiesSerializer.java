package com.blue.basic.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;
import static java.util.stream.Collectors.toList;

/**
 * ids long转string
 *
 * @author liuyunfei
 */
public final class IdentitiesSerializer extends JsonSerializer<List<Long>> {

    @Override
    public void serialize(List<Long> value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (isNotEmpty(value))
            jsonGenerator.writeObject(value.stream().map(String::valueOf).collect(toList()));
    }

}
