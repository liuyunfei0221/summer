package com.blue.database.type;

import com.blue.basic.component.encoder.api.common.StringColumnEncoder;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.blue.basic.component.encoder.api.common.StringColumnEncoder.encryptString;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;

/**
 * encode string type handler
 *
 * @author liuyunfei
 *
 * <p>
 * actived by
 * @see com.blue.basic.component.encoder.api.conf.EncoderConf
 * @see com.blue.basic.component.encoder.api.conf.EncoderConfParams
 */
public final class StringColumnEncodeTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, encryptString(parameter));
    }

    @Override
    public String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return ofNullable(resultSet.getString(columnName))
                .filter(StringUtils::hasText)
                .map(StringColumnEncoder::decryptString)
                .orElse(EMPTY_VALUE.value);
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return ofNullable(resultSet.getString(columnIndex))
                .filter(StringUtils::hasText)
                .map(StringColumnEncoder::decryptString)
                .orElse(EMPTY_VALUE.value);
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return ofNullable(callableStatement.getString(columnIndex))
                .filter(StringUtils::hasText)
                .map(StringColumnEncoder::decryptString)
                .orElse(EMPTY_VALUE.value);
    }

}
