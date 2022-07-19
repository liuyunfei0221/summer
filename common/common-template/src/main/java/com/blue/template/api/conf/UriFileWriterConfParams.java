package com.blue.template.api.conf;

/**
 * uri file writer params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class UriFileWriterConfParams implements UriFileWriterConf {

    private String uri;

    public UriFileWriterConfParams() {
    }

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "PackageFileWriterConfParams{" +
                "uri='" + uri + '\'' +
                '}';
    }

}
