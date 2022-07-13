package com.blue.template.api.generator;

import com.blue.template.api.conf.PackageFileWriterConf;
import com.blue.template.api.conf.StringContentWriterConf;
import com.blue.template.component.PackageFileTemplateWriter;
import com.blue.template.component.StringContentTemplateWriter;

/**
 * template writer generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class TemplateWriterGenerator {

    /**
     * generate package file template writer
     *
     * @param packageFileWriterConf
     * @return
     */
    public static PackageFileTemplateWriter generatePackageFileTemplateWriter(PackageFileWriterConf packageFileWriterConf) {
        return new PackageFileTemplateWriter(packageFileWriterConf);
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
