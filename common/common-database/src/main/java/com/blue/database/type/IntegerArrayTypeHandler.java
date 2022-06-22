package com.blue.database.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * int array type handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class IntegerArrayTypeHandler extends BaseTypeHandler<Integer[]> {

    private static final String JDBC_TYPE_NAME = JdbcType.INTEGER.name();

    private static final BiFunction<Object[], PreparedStatement, Array> ARRAY_GEN = (parameter, preparedStatement) -> {
        try {
            return preparedStatement.getConnection().createArrayOf(JDBC_TYPE_NAME, parameter);
        } catch (SQLException e) {
            throw new RuntimeException("IntegerArrayTypeHandler, ARRAY_GEN failed, e = " + e);
        }
    };

    private static final Function<Array, Integer[]> ARRAY_PARSER = array -> {
        try {
            return array != null ? (Integer[]) array.getArray() : null;
        } catch (SQLException e) {
            throw new RuntimeException("IntegerArrayTypeHandler, ARRAY_PARSER failed, e = " + e);
        }
    };

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Integer[] parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setArray(i, ARRAY_GEN.apply(parameter, preparedStatement));
    }

    @Override
    public Integer[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return ARRAY_PARSER.apply(resultSet.getArray(columnName));
    }

    @Override
    public Integer[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return ARRAY_PARSER.apply(resultSet.getArray(columnIndex));
    }

    @Override
    public Integer[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return ARRAY_PARSER.apply(callableStatement.getArray(columnIndex));
    }

}
