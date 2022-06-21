package com.blue.database.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNotNull;

/**
 * string array type handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StringArrayTypeHandler extends BaseTypeHandler<String[]> {

    private static final String JDBC_TYPE_NAME = JdbcType.VARCHAR.name();

    private static final Object[] NULL_PARAM = null;

    private static final BiFunction<Object[], PreparedStatement, Array> ARRAY_GEN = (parameter, preparedStatement) -> {
        try {
            return preparedStatement.getConnection().createArrayOf(JDBC_TYPE_NAME, isNotNull(parameter) ? parameter : NULL_PARAM);
        } catch (SQLException e) {
            throw new RuntimeException("StringArrayTypeHandler, ARRAY_GEN failed, e = " + e);
        }
    };

    private static final Function<Array, String[]> ARRAY_PARSER = array -> {
        try {
            return array != null ? (String[]) array.getArray() : null;
        } catch (SQLException e) {
            throw new RuntimeException("StringArrayTypeHandler, ARRAY_PARSER failed, e = " + e);
        }
    };

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setArray(i, ARRAY_GEN.apply(parameter, preparedStatement));
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return ARRAY_PARSER.apply(resultSet.getArray(columnName));
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return ARRAY_PARSER.apply(resultSet.getArray(columnIndex));
    }

    @Override
    public String[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return ARRAY_PARSER.apply(callableStatement.getArray(columnIndex));
    }

}
