package com.blue.member.repository.type;

import com.blue.base.common.base.AesProcessor;
import com.blue.member.config.deploy.SecretDeploy;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.util.Logger;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * encrypt type handler
 *
 * @author liuyunfei
 * @date 2021/10/12
 * @apiNote
 */
@Component
public class SecretStringTypeHandler extends BaseTypeHandler<String> {

    private static final Logger LOGGER = getLogger(SecretStringTypeHandler.class);

    private static AesProcessor aesProcessor;

    @Autowired
    public void setSecretDeploy(SecretDeploy secretDeploy) {
        LOGGER.warn("secretDeploy = {}", secretDeploy);
        aesProcessor = new AesProcessor(secretDeploy.getSalt());
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, aesProcessor.encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ofNullable(rs.getString(columnName))
                .filter(StringUtils::hasText)
                .map(aesProcessor::decrypt)
                .orElse("");
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ofNullable(rs.getString(columnIndex))
                .filter(StringUtils::hasText)
                .map(aesProcessor::decrypt)
                .orElse("");
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ofNullable(cs.getString(columnIndex))
                .filter(StringUtils::hasText)
                .map(aesProcessor::decrypt)
                .orElse("");
    }
}
