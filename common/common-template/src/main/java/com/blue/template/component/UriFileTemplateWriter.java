package com.blue.template.component;

import com.blue.template.api.conf.UriFileWriterConf;
import com.blue.template.common.TemplateWriter;
import com.blue.template.model.WriterElement;
import freemarker.template.Configuration;
import freemarker.template.Template;
import reactor.util.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.FileGetter.getFiles;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.ResourceUtils.getURL;
import static reactor.util.Loggers.getLogger;

/**
 * template writer from files of uri
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public class UriFileTemplateWriter implements TemplateWriter {

    private static final Logger LOGGER = getLogger(UriFileTemplateWriter.class);

    private static final Map<String, Object> EMPTY_DATA = new HashMap<>();

    private Configuration configuration;

    private Set<String> templateNames;

    public UriFileTemplateWriter(UriFileWriterConf uriFileWriterConf) {
        WriterElement writerElement = generateElement(uriFileWriterConf);

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
     * @param uriFileWriterConf
     */
    public void reloadTemplates(UriFileWriterConf uriFileWriterConf) {
        synchronized (this) {
            WriterElement writerElement = generateElement(uriFileWriterConf);
            this.configuration = writerElement.getConfiguration();
            this.templateNames = writerElement.getTemplateNames();
        }
    }

    /**
     * generate writer elements
     *
     * @param uriFileWriterConf
     * @return
     */
    private static WriterElement generateElement(UriFileWriterConf uriFileWriterConf) {
        LOGGER.info("static WriterElement generateElement(PackageFileWriterConf packageFileWriterConf), packageFileWriterConf = {}", uriFileWriterConf);

        confAssert(uriFileWriterConf);

        String uri = uriFileWriterConf.getUri();

        List<File> templateFiles = getFiles(uri, true);
        LOGGER.info("templateFiles = {}", templateFiles);

        Set<String> templateNames = new HashSet<>(templateFiles.size(), 1.0f);
        for (File file : templateFiles)
            templateNames.add(file.getName());

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

        try {
            configuration.setDirectoryForTemplateLoading(new File(getURL(uri).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        configuration.setDefaultEncoding(UTF_8.name());

        return new WriterElement(configuration, templateNames);
    }

    /**
     * assert conf
     *
     * @param uriFileWriterConf
     */
    private static void confAssert(UriFileWriterConf uriFileWriterConf) {
        if (isNull(uriFileWriterConf))
            throw new RuntimeException("packageFileWriterConf can't be null");

        if (isBlank(uriFileWriterConf.getUri()))
            throw new RuntimeException("uri can't be blank");
    }

}
