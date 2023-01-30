package com.blue.template.api.conf;

import java.io.Serializable;

/**
 * string content attr
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StringContentAttr implements Serializable {

    private static final long serialVersionUID = -7736949586524198962L;

    private String templateName;

    private String templateContent;

    public StringContentAttr() {
    }

    public StringContentAttr(String templateName, String templateContent) {
        this.templateName = templateName;
        this.templateContent = templateContent;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    @Override
    public String toString() {
        return "StringContentAttr{" +
                "templateName='" + templateName + '\'' +
                ", templateContent='" + templateContent + '\'' +
                '}';
    }

}
