package com.blue.template.component;

import com.blue.template.api.conf.StringContentAttr;
import com.blue.template.api.conf.StringContentWriterConf;
import com.blue.template.core.TemplateWriter;
import com.blue.template.model.WriterElement;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import reactor.util.Logger;

import java.io.Writer;
import java.util.*;

import static com.blue.basic.common.base.BlueChecker.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static reactor.util.Loggers.getLogger;

/**
 * template writer from string contents
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public class StringContentTemplateWriter implements TemplateWriter {

    private static final Logger LOGGER = getLogger(StringContentTemplateWriter.class);

    private static final Map<String, Object> EMPTY_DATA = new HashMap<>();

    private Configuration configuration;

    private Set<String> templateNames;

    public StringContentTemplateWriter(StringContentWriterConf stringContentWriterConf) {
        WriterElement writerElement = generateElement(stringContentWriterConf);

        this.configuration = writerElement.getConfiguration();
        this.templateNames = writerElement.getTemplateNames();
    }

    /**
     * show all template name
     *
     * @return
     */
    @Override
    public Set<String> showTemplateNames() {
        return templateNames;
    }

    /**
     * get template by unique name
     *
     * @param templateName
     * @return
     */
    @Override
    public Template getTemplate(String templateName) {
        if (isBlank(templateName))
            throw new RuntimeException("templateName can't be blank");

        if (!templateNames.contains(templateName))
            throw new RuntimeException("the template which name is " + templateName + " doesn't exist");

        try {
            Template template = configuration.getTemplate(templateName);

            if (isNull(template))
                throw new RuntimeException("the template which name is " + templateName + " doesn't exist");

            return template;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        if (isNull(writer))
            throw new RuntimeException("writer can't be null");

        Template template = this.getTemplate(templateName);

        try {
            template.process(isNotNull(data) ? data : EMPTY_DATA, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * reload templates
     *
     * @param stringContentWriterConf
     */
    public void reloadTemplates(StringContentWriterConf stringContentWriterConf) {
        synchronized (this) {
            WriterElement writerElement = generateElement(stringContentWriterConf);
            this.configuration = writerElement.getConfiguration();
            this.templateNames = writerElement.getTemplateNames();
        }
    }

    /**
     * generate writer elements
     *
     * @param stringContentWriterConf
     * @return
     */
    private static WriterElement generateElement(StringContentWriterConf stringContentWriterConf) {
        LOGGER.info("static WriterElement generateElement(StringContentWriterConf stringContentWriterConf), stringContentWriterConf = {}", stringContentWriterConf);

        confAssert(stringContentWriterConf);

        List<StringContentAttr> stringContentAttrs = stringContentWriterConf.getStringContentAttrs();

        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        Set<String> templateNames = new HashSet<>(stringContentAttrs.size(), 1.0f);

        String templateName;
        for (StringContentAttr attr : stringContentAttrs) {
            templateName = attr.getTemplateName();
            stringTemplateLoader.putTemplate(templateName, attr.getTemplateContent());
            templateNames.add(templateName);
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setTemplateLoader(stringTemplateLoader);
        configuration.setDefaultEncoding(UTF_8.name());

        return new WriterElement(configuration, templateNames);
    }

    /**
     * assert conf
     *
     * @param stringContentWriterConf
     */
    private static void confAssert(StringContentWriterConf stringContentWriterConf) {
        if (isNull(stringContentWriterConf))
            throw new RuntimeException("stringContentWriterConf can't be null");

        if (isEmpty(stringContentWriterConf.getStringContentAttrs()))
            throw new RuntimeException("stringContentAttrs can't be empty");
    }

}
