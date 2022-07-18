package com.blue.template.component;

import com.blue.template.api.conf.PackageFileWriterConf;
import com.blue.template.common.TemplateWriter;
import com.blue.template.model.WriterElement;
import freemarker.template.Configuration;
import freemarker.template.Template;
import reactor.util.Logger;

import java.io.File;
import java.io.Writer;
import java.util.*;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.FileGetter.getFiles;
import static java.nio.charset.StandardCharsets.UTF_8;
import static reactor.util.Loggers.getLogger;

/**
 * template writer from files of package
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public class PackageFileTemplateWriter implements TemplateWriter {

    private static final Logger LOGGER = getLogger(PackageFileTemplateWriter.class);

    private static final Map<String, Object> EMPTY_DATA = new HashMap<>();

    private Configuration configuration;

    private Set<String> templateNames;

    public PackageFileTemplateWriter(PackageFileWriterConf packageFileWriterConf) {
        WriterElement writerElement = generateElement(packageFileWriterConf);

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

        if (templateNames.contains(templateName))
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
     * @param packageFileWriterConf
     */
    public void reloadTemplates(PackageFileWriterConf packageFileWriterConf) {
        synchronized (this) {
            WriterElement writerElement = generateElement(packageFileWriterConf);
            this.configuration = writerElement.getConfiguration();
            this.templateNames = writerElement.getTemplateNames();
        }
    }

    /**
     * generate writer elements
     *
     * @param packageFileWriterConf
     * @return
     */
    private static WriterElement generateElement(PackageFileWriterConf packageFileWriterConf) {
        LOGGER.info("static WriterElement generateElement(PackageFileWriterConf packageFileWriterConf), packageFileWriterConf = {}", packageFileWriterConf);

        confAssert(packageFileWriterConf);

        String basePackagePath = packageFileWriterConf.getBasePackagePath();

        List<File> templateFiles = getFiles(basePackagePath, true);
        LOGGER.info("templateFiles = {}", templateFiles);

        Set<String> templateNames = new HashSet<>(templateFiles.size(), 1.0f);
        for (File file : templateFiles)
            templateNames.add(file.getName());

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(PackageFileTemplateWriter.class, basePackagePath);
        configuration.setDefaultEncoding(UTF_8.name());

        return new WriterElement(configuration, templateNames);
    }

    /**
     * assert conf
     *
     * @param packageFileWriterConf
     */
    private static void confAssert(PackageFileWriterConf packageFileWriterConf) {
        if (isNull(packageFileWriterConf))
            throw new RuntimeException("packageFileWriterConf can't be null");

        if (isBlank(packageFileWriterConf.getBasePackagePath()))
            throw new RuntimeException("basePackagePath can't be blank");
    }

}
