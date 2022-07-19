package com.blue.template.api.generator;

import com.blue.template.api.conf.UriFileWriterConf;
import com.blue.template.api.conf.StringContentWriterConf;
import com.blue.template.component.UriFileTemplateWriter;
import com.blue.template.component.StringContentTemplateWriter;

/**
 * template writer generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class TemplateWriterGenerator {

    /**
     * generate uri file template writer
     *
     * @param uriFileWriterConf
     * @return
     */
    public static UriFileTemplateWriter generateUriFileTemplateWriter(UriFileWriterConf uriFileWriterConf) {
        return new UriFileTemplateWriter(uriFileWriterConf);
    }

    /**
     * generate string content template writer
     *
     * @param stringContentWriterConf
     * @return
     */
    public static StringContentTemplateWriter generateStringContentTemplateWriter(StringContentWriterConf stringContentWriterConf) {
        return new StringContentTemplateWriter(stringContentWriterConf);
    }

}
