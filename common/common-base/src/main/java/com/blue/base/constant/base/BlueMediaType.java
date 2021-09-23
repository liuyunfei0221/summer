package com.blue.base.constant.base;

import org.springframework.http.MediaType;

/**
 * 媒体类型
 *
 * @author liuyunfei
 * @date 2021/9/5
 * @apiNote
 */
@SuppressWarnings({"AlibabaEnumConstantsMustHaveComment", "deprecation"})
public enum BlueMediaType {

    ALL("*", MediaType.ALL, "*"),
    APPLICATION_ATOM_XML("application/atom+xml", MediaType.APPLICATION_ATOM_XML, "application/atom+xml"),
    APPLICATION_CBOR("application/cbor", MediaType.APPLICATION_CBOR, "application/cbor"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded", MediaType.APPLICATION_FORM_URLENCODED, "application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json", MediaType.APPLICATION_JSON, "application/json"),
    APPLICATION_OCTET_STREAM("application/octet-stream", MediaType.APPLICATION_OCTET_STREAM, "application/octet-stream"),
    APPLICATION_PDF("application/pdf", MediaType.APPLICATION_PDF, "application/pdf"),
    APPLICATION_PROBLEM_JSON("application/problem+json", MediaType.APPLICATION_PROBLEM_JSON, "application/problem+json"),
    APPLICATION_PROBLEM_XML("application/problem+xml", MediaType.APPLICATION_PROBLEM_XML, "application/problem+xml"),

    APPLICATION_RSS_XML("application/rss+xml", MediaType.APPLICATION_RSS_XML, "application/rss+xml"),
    APPLICATION_NDJSON("application/x-ndjson", MediaType.APPLICATION_NDJSON, "application/x-ndjson"),
    APPLICATION_XHTML_XML("application/xhtml+xml", MediaType.APPLICATION_XHTML_XML, "application/xhtml+xml"),
    APPLICATION_XML("application/xml", MediaType.APPLICATION_XML, "application/xml"),
    IMAGE_GIF("image/gif", MediaType.IMAGE_GIF, "image/gif"),
    IMAGE_JPEG("image/jpeg", MediaType.IMAGE_JPEG, "image/jpeg"),
    IMAGE_PNG("image/png", MediaType.IMAGE_PNG, "image/png"),
    MULTIPART_FORM_DATA("multipart/form-data", MediaType.MULTIPART_FORM_DATA, "multipart/form-data"),
    MULTIPART_MIXED("multipart/mixed", MediaType.MULTIPART_MIXED, "multipart/mixed"),
    MULTIPART_RELATED("multipart/related", MediaType.MULTIPART_RELATED, "multipart/related"),
    TEXT_EVENT_STREAM("text/event-stream", MediaType.TEXT_EVENT_STREAM, "text/event-stream"),
    TEXT_HTML("text/html", MediaType.TEXT_HTML, "text/html"),
    TEXT_MARKDOWN("text/markdown", MediaType.TEXT_MARKDOWN, "text/markdown"),
    TEXT_PLAIN("text/plain", MediaType.TEXT_PLAIN, "text/plain"),
    APPLICATION_JSON_UTF8("application/json;charset=UTF-8", MediaType.APPLICATION_JSON_UTF8, "application/json;charset=UTF-8"),
    APPLICATION_PROBLEM_JSON_UTF8("application/problem+json;charset=UTF-8", MediaType.APPLICATION_PROBLEM_JSON_UTF8, "application/problem+json;charset=UTF-8"),
    APPLICATION_STREAM_JSON("application/stream+json", MediaType.APPLICATION_STREAM_JSON, "application/stream+json");

    public final String identity;

    public final MediaType mediaType;

    public final String disc;

    BlueMediaType(String identity, MediaType mediaType, String disc) {
        this.identity = identity;
        this.mediaType = mediaType;
        this.disc = disc;
    }
}
