package com.blue.database.type;

import java.math.BigDecimal;

import static org.apache.ibatis.type.JdbcType.*;

/**
 * java clz name and jdbc type name mapping
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public enum DataType {

    /**
     * short
     */
    SHORT_MAPPING(Short.class.getName(), SMALLINT.name()),

    /**
     * integer
     */
    INTEGER_MAPPING(Integer.class.getName(), INTEGER.name()),

    /**
     * long
     */
    LONG_MAPPING(Long.class.getName(), BIGINT.name()),

    /**
     * float
     */
    FLOAT_MAPPING(Float.class.getName(), FLOAT.name()),

    /**
     * double
     */
    DOUBLE_MAPPING(Double.class.getName(), DOUBLE.name()),

    /**
     * big decimal
     */
    BIG_DECIMAL_MAPPING(BigDecimal.class.getName(), NUMERIC.name()),

    /**
     * boolean
     */
    BOOLEAN_MAPPING(Boolean.class.getName(), BOOLEAN.name()),

    /**
     * string
     */
    STRING(String.class.getName(), VARCHAR.name());

    /**
     * java clz name
     */
    public final String javaClzName;

    /**
     * jdbc type name
     */
    public final String jdbcTypeName;

    DataType(String javaClzName, String jdbcTypeName) {
        this.javaClzName = javaClzName;
        this.jdbcTypeName = jdbcTypeName;
    }

}
