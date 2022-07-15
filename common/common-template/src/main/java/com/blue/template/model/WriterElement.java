package com.blue.template.model;

import freemarker.template.Configuration;

import java.util.Set;

import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * template writer elements
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class WriterElement {

    private final Configuration configuration;

    private final Set<String> templateNames;

    public WriterElement(Configuration configuration, Set<String> templateNames) {
        if (isNull(configuration) || isNull(templateNames))
            throw new RuntimeException("configuration or templateNames can't be null");

        this.configuration = configuration;
        this.templateNames = templateNames;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Set<String> getTemplateNames() {
        return templateNames;
    }

    @Override
    public String toString() {
        return "WriterElement{" +
                "configuration=" + configuration +
                ", templateNames=" + templateNames +
                '}';
    }

}
