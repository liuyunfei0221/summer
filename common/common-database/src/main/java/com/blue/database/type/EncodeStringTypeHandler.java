package com.blue.database.type;

import com.blue.base.common.base.AesProcessor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.blue.database.type.StringEncoderHolder.getProcessor;
import static java.util.Optional.ofNullable;

/**
 * encode string type handler
 *
 * @author liuyunfei
 * @date 2021/10/12
 * @apiNote
 */
public final class EncodeStringTypeHandler extends BaseTypeHandler<String> {

    private static final AesProcessor AES_PROCESSOR = getProcessor();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, AES_PROCESSOR.encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return ofNullable(resultSet.getString(columnName))
                .filter(StringUtils::hasText)
                .map(AES_PROCESSOR::decrypt)
                .orElse("");
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return ofNullable(resultSet.getString(columnIndex))
                .filter(StringUtils::hasText)
                .map(AES_PROCESSOR::decrypt)
                .orElse("");
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return ofNullable(callableStatement.getString(columnIndex))
                .filter(StringUtils::hasText)
                .map(AES_PROCESSOR::decrypt)
                .orElse("");
    }

}
