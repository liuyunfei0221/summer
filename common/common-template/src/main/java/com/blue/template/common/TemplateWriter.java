package com.blue.template.common;

import freemarker.template.Template;

import java.io.Writer;
import java.util.Set;

/**
 * template writer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface TemplateWriter {

    /**
     * show all template name
     *
     * @return
     */
    Set<String> showTemplateNames();

    /**
     * get template by unique name
     *
     * @param templateName
     * @return
     */
    Template getTemplate(String templateName);

    /**
     * write the file which generated to writer
     *
     * @param templateName
     * @param data
     * @param writer
     */
    void write(String templateName, Object data, Writer writer);

}
