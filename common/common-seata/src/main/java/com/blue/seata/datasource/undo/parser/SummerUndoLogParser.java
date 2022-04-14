package com.blue.seata.datasource.undo.parser;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.seata.common.executor.Initialize;
import io.seata.common.loader.EnhancedServiceLoader;
import io.seata.common.loader.EnhancedServiceNotFoundException;
import io.seata.common.loader.LoadLevel;
import io.seata.rm.datasource.undo.BranchUndoLog;
import io.seata.rm.datasource.undo.UndoLogParser;
import io.seata.rm.datasource.undo.parser.spi.JacksonSerializer;
import org.slf4j.Logger;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.MapperFeature.PROPAGATE_TRANSIENT_MARKER;
import static io.seata.common.Constants.DEFAULT_CHARSET;
import static io.seata.common.util.CollectionUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * localdatetime parser
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unchecked", "rawtypes", "deprecation", "AliControlFlowStatementWithoutBraces", "SpellCheckingInspection"})
@LoadLevel(name = SummerUndoLogParser.NAME)
public final class SummerUndoLogParser implements UndoLogParser, Initialize {

    public static final String NAME = "jackson";

    private static final Logger LOGGER = getLogger(SummerUndoLogParser.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private final SimpleModule module = new SimpleModule();

    private final JsonSerializer timestampSerializer = new TimestampSerializer();

    private final JsonDeserializer timestampDeserializer = new TimestampDeserializer();

    private final JsonSerializer localDateTimeSerializer = new LocalDateTimeSerializer();

    private final JsonDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer();

    private final JsonSerializer blobSerializer = new BlobSerializer();

    private final JsonDeserializer blobDeserializer = new BlobDeserializer();

    private final JsonSerializer clobSerializer = new ClobSerializer();

    private final JsonDeserializer clobDeserializer = new ClobDeserializer();

    @Override
    public void init() {
        try {
            List<JacksonSerializer> jacksonSerializers = EnhancedServiceLoader.loadAll(JacksonSerializer.class);
            if (isNotEmpty(jacksonSerializers)) {
                Class type;
                JsonSerializer ser;
                JsonDeserializer deser;
                for (JacksonSerializer jacksonSerializer : jacksonSerializers) {
                    type = jacksonSerializer.type();
                    ser = jacksonSerializer.ser();
                    deser = jacksonSerializer.deser();
                    if (type != null) {
                        if (ser != null)
                            module.addSerializer(type, ser);
                        if (deser != null)
                            module.addDeserializer(type, deser);
                        LOGGER.info("jackson undo log parser load [{}].", jacksonSerializer.getClass().getName());
                    }
                }
            }
        } catch (EnhancedServiceNotFoundException e) {
            LOGGER.warn("JacksonSerializer not found children class.", e);
        }

        module.addSerializer(Timestamp.class, timestampSerializer);
        module.addDeserializer(Timestamp.class, timestampDeserializer);
        module.addSerializer(SerialBlob.class, blobSerializer);
        module.addDeserializer(SerialBlob.class, blobDeserializer);
        module.addSerializer(SerialClob.class, clobSerializer);
        module.addDeserializer(SerialClob.class, clobDeserializer);
        module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        mapper.registerModule(module);
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        mapper.enable(PROPAGATE_TRANSIENT_MARKER);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public byte[] getDefaultContent() {
        return "{}".getBytes(DEFAULT_CHARSET);
    }

    @Override
    public byte[] encode(BranchUndoLog branchUndoLog) {
        try {
            return mapper.writeValueAsBytes(branchUndoLog);
        } catch (JsonProcessingException e) {
            LOGGER.error("json encode exception, {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public BranchUndoLog decode(byte[] bytes) {
        try {
            BranchUndoLog branchUndoLog;
            if (Arrays.equals(bytes, getDefaultContent())) {
                branchUndoLog = new BranchUndoLog();
            } else {
                branchUndoLog = mapper.readValue(bytes, BranchUndoLog.class);
            }
            return branchUndoLog;
        } catch (IOException e) {
            LOGGER.error("json decode exception, {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static class TimestampSerializer extends JsonSerializer<Timestamp> {
        @Override
        public void serializeWithType(Timestamp timestamp, JsonGenerator gen, SerializerProvider serializers,
                                      TypeSerializer typeSerializer) throws IOException {
            WritableTypeId typeId = typeSerializer.writeTypePrefix(gen,
                    typeSerializer.typeId(timestamp, JsonToken.START_ARRAY));
            serialize(timestamp, gen, serializers);
            gen.writeTypeSuffix(typeId);
        }

        @Override
        public void serialize(Timestamp timestamp, JsonGenerator gen, SerializerProvider serializers) {
            try {
                gen.writeNumber(timestamp.getTime());
                gen.writeNumber(timestamp.getNanos());
            } catch (IOException e) {
                LOGGER.error("serialize java.sql.Timestamp error : {}", e.getMessage(), e);
            }
        }
    }

    private static class TimestampDeserializer extends JsonDeserializer<Timestamp> {
        @Override
        public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) {
            if (p.isExpectedStartArrayToken()) {
                ArrayNode arrayNode;
                try {
                    arrayNode = p.getCodec().readTree(p);
                    Timestamp timestamp = new Timestamp(arrayNode.get(0).asLong());
                    timestamp.setNanos(arrayNode.get(1).asInt());
                    return timestamp;
                } catch (IOException e) {
                    LOGGER.error("deserialize java.sql.Timestamp error : {}", e.getMessage(), e);
                }
            }
            LOGGER.error("deserialize java.sql.Timestamp type error.");
            return null;
        }
    }

    private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serializeWithType(LocalDateTime localDateTime, JsonGenerator gen, SerializerProvider serializers,
                                      TypeSerializer typeSerializer) throws IOException {
            WritableTypeId typeId = typeSerializer.writeTypePrefix(gen,
                    typeSerializer.typeId(localDateTime, JsonToken.START_ARRAY));
            serialize(localDateTime, gen, serializers);
            gen.writeTypeSuffix(typeId);
        }

        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator gen, SerializerProvider serializers) {
            try {
                gen.writeNumber(localDateTime.getYear());
                gen.writeNumber(localDateTime.getMonthValue());
                gen.writeNumber(localDateTime.getDayOfMonth());
                gen.writeNumber(localDateTime.getHour());
                gen.writeNumber(localDateTime.getMinute());
                gen.writeNumber(localDateTime.getSecond());
                gen.writeNumber(localDateTime.getNano());
            } catch (IOException e) {
                LOGGER.error("serialize java.time.LocalDateTime error : {}", e.getMessage(), e);
            }
        }
    }

    private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) {
            if (p.isExpectedStartArrayToken()) {
                ArrayNode arrayNode;
                try {
                    arrayNode = p.getCodec().readTree(p);
                    return LocalDateTime.of(arrayNode.get(0).asInt(), arrayNode.get(1).asInt(), arrayNode.get(2).asInt(),
                            arrayNode.get(3).asInt(), arrayNode.get(4).asInt(), arrayNode.get(5).asInt(), arrayNode.get(6).asInt());
                } catch (IOException e) {
                    LOGGER.error("deserialize java.time.LocalDateTime error : {}", e.getMessage(), e);
                }
            }
            LOGGER.error("deserialize java.time.LocalDateTime type error.");
            return null;
        }
    }

    private static class BlobSerializer extends JsonSerializer<SerialBlob> {
        @Override
        public void serializeWithType(SerialBlob blob, JsonGenerator gen, SerializerProvider serializers,
                                      TypeSerializer typeSer) throws IOException {
            WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen,
                    typeSer.typeId(blob, JsonToken.VALUE_EMBEDDED_OBJECT));
            serialize(blob, gen, serializers);
            typeSer.writeTypeSuffix(gen, typeIdDef);
        }

        @Override
        public void serialize(SerialBlob blob, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            try {
                gen.writeBinary(blob.getBytes(1, (int) blob.length()));
            } catch (SerialException e) {
                LOGGER.error("serialize java.sql.Blob error : {}", e.getMessage(), e);
            }
        }
    }

    private static class BlobDeserializer extends JsonDeserializer<SerialBlob> {
        @Override
        public SerialBlob deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            try {
                return new SerialBlob(p.getBinaryValue());
            } catch (SQLException e) {
                LOGGER.error("deserialize java.sql.Blob error : {}", e.getMessage(), e);
            }
            return null;
        }
    }

    private static class ClobSerializer extends JsonSerializer<SerialClob> {
        @Override
        public void serializeWithType(SerialClob clob, JsonGenerator gen, SerializerProvider serializers,
                                      TypeSerializer typeSer) throws IOException {
            WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen,
                    typeSer.typeId(clob, JsonToken.VALUE_EMBEDDED_OBJECT));
            serialize(clob, gen, serializers);
            typeSer.writeTypeSuffix(gen, typeIdDef);
        }

        @Override
        public void serialize(SerialClob clob, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            try {
                gen.writeString(clob.getCharacterStream(), (int) clob.length());
            } catch (SerialException e) {
                LOGGER.error("serialize java.sql.Blob error : {}", e.getMessage(), e);
            }
        }
    }

    private static class ClobDeserializer extends JsonDeserializer<SerialClob> {
        @Override
        public SerialClob deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            try {
                return new SerialClob(p.getValueAsString().toCharArray());

            } catch (SQLException e) {
                LOGGER.error("deserialize java.sql.Clob error : {}", e.getMessage(), e);
            }
            return null;
        }
    }

}
