package com.blue.template.component;

import com.blue.template.api.conf.PackageFileWriterConf;
import com.blue.template.common.TemplateWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * template writer from files of package
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public class PackageFileTemplateWriter implements TemplateWriter {

    private static final Map<String, Object> EMPTY_DATA = new HashMap<>();

    private Configuration configuration;

    private Set<String> templateNames;

    public PackageFileTemplateWriter(PackageFileWriterConf packageFileWriterConf) {
        //TODO
    }

    /**
     * show all template name
     *
     * @return
     */
    @Override
    public Set<String> showTemplateNames() {
        return null;
    }

    /**
     * get template by unique name
     *
     * @param templateName
     * @return
     */
    @Override
    public Template getTemplate(String templateName) {
        return null;
    }

    /**
     * write the file which generated to writer
     *
     * @param templateName
     * @param data
     * @param writer
     */
    @Override
    public void write(String templateName, Object data, Writer writer) {

    }

}
