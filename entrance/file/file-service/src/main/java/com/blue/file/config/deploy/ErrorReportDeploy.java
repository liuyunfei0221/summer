package com.blue.file.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 加密数据过期时间配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "error")
public class ErrorReportDeploy {

    private List<String> errorReportWithRequestBodyContentTypes;

    public ErrorReportDeploy() {
    }

    public List<String> getErrorReportWithRequestBodyContentTypes() {
        return errorReportWithRequestBodyContentTypes;
    }

    public void setErrorReportWithRequestBodyContentTypes(List<String> errorReportWithRequestBodyContentTypes) {
        this.errorReportWithRequestBodyContentTypes = errorReportWithRequestBodyContentTypes;
    }

    @Override
    public String toString() {
        return "ErrorReportDeploy{" +
                "errorReportWithRequestBodyContentTypes=" + errorReportWithRequestBodyContentTypes +
                '}';
    }

}
