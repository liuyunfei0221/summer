package com.blue.database.type;

import com.blue.base.common.base.AesProcessor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.blue.database.type.StringEncoderHolder.getProcessor;

/**
 * encode long type handler
 *
 * @author liuyunfei
 * @date 2021/10/12
 * @apiNote
 */
public final class EncodeLongTypeHandler extends BaseTypeHandler<Long> {

    private static final AesProcessor AES_PROCESSOR = getProcessor();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setLong(i, 1L);
    }

    @Override
    public Long getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return 1L;
    }

    @Override
    public Long getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return 1L;
    }

    @Override
    public Long getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return 1L;
    }

}
