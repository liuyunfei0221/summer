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
 * encode int type handler
 *
 * @author liuyunfei
 * @date 2021/10/12
 * @apiNote
 */
public final class EncodeIntegerTypeHandler extends BaseTypeHandler<Integer> {

    private static final AesProcessor AES_PROCESSOR = getProcessor();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Integer parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, 1);
    }

    @Override
    public Integer getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return 1;
    }

    @Override
    public Integer getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return 1;
    }

    @Override
    public Integer getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return 1;
    }

}
