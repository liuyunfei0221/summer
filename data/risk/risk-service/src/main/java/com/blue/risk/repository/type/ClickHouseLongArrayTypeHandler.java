package com.blue.risk.repository.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import ru.yandex.clickhouse.ClickHouseArray;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static java.util.Arrays.stream;

/**
 * click house long array type handler
 *
 * @author liuyunfei
 */
public final class ClickHouseLongArrayTypeHandler extends BaseTypeHandler<Long[]> {

    private static final Function<Object, Long[]> ARRAY_PARSER = obj -> {
        try {
            return obj != null ? stream((BigInteger[]) ((ClickHouseArray) obj).getArray()).map(BigInteger::longValue).toArray(Long[]::new) : null;
        } catch (SQLException e) {
            throw new RuntimeException("ClickHouseLongArrayTypeHandler, ARRAY_PARSER failed, e = " + e);
        }
    };

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Long[] parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i, parameter);
    }

    @Override
    public Long[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return ARRAY_PARSER.apply(resultSet.getObject(columnName));
    }

    @Override
    public Long[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return ARRAY_PARSER.apply(resultSet.getObject(columnIndex));
    }

    @Override
    public Long[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return ARRAY_PARSER.apply(callableStatement.getObject(columnIndex));
    }

}