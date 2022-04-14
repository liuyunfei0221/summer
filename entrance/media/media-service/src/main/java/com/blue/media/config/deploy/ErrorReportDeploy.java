package com.blue.media.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * encrypt config
 *
 * @author liuyunfei
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
