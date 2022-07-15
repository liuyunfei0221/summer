package com.blue.database.type;

import com.blue.basic.common.base.BlueChecker;
import com.blue.database.constant.DataType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * array type handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("ConstantConditions")
@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.ARRAY})
public final class ArrayTypeHandler extends BaseTypeHandler<Object[]> {

    private static final Object[] NULL_PARAM = null;

    private static final Map<String, String> ELEMENT_TYPE_MAPPING = Stream.of(DataType.values())
            .collect(toMap(dt -> dt.javaClzName, dt -> dt.jdbcTypeName, (a, b) -> a));

    private static final BiFunction<Object[], PreparedStatement, Array> ARRAY_GEN = (parameter, preparedStatement) ->
            ofNullable(parameter.getClass().getComponentType().getName())
                    .map(ELEMENT_TYPE_MAPPING::get)
                    .filter(BlueChecker::isNotNull)
                    .map(typeName -> {
                        try {
                            return preparedStatement.getConnection().createArrayOf(typeName, isNotNull(parameter) ? parameter : NULL_PARAM);
                        } catch (SQLException e) {
                            throw new RuntimeException("ARRAY_GEN failed, e = " + e);
                        }
                    })
                    .orElseThrow(() -> new RuntimeException("UnSupport parameter type with ArrayTypeHandler"));


    private static final Function<Array, Object[]> ARRAY_PARSER = array -> {
        try {
            return array != null ? (Object[]) array.getArray() : null;
        } catch (SQLException e) {
            throw new RuntimeException("parseArray(Array array) failed, e = " + e);
        }
    };

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object[] parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setArray(i, ARRAY_GEN.apply(parameter, preparedStatement));
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return ARRAY_PARSER.apply(resultSet.getArray(columnName));
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return ARRAY_PARSER.apply(resultSet.getArray(columnIndex));
    }

    @Override
    public Object[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return ARRAY_PARSER.apply(callableStatement.getArray(columnIndex));
    }

}
