package com.blue.base.constant.common;

import org.springframework.http.MediaType;

/**
 * media type
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaEnumConstantsMustHaveComment", "deprecation"})
public enum BlueMediaType {

    ALL("*", MediaType.ALL),
    APPLICATION_ATOM_XML("application/atom+xml", MediaType.APPLICATION_ATOM_XML),
    APPLICATION_CBOR("application/cbor", MediaType.APPLICATION_CBOR),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded", MediaType.APPLICATION_FORM_URLENCODED),
    APPLICATION_JSON("application/json", MediaType.APPLICATION_JSON),
    APPLICATION_OCTET_STREAM("application/octet-stream", MediaType.APPLICATION_OCTET_STREAM),
    APPLICATION_PDF("application/pdf", MediaType.APPLICATION_PDF),
    APPLICATION_PROBLEM_JSON("application/problem+json", MediaType.APPLICATION_PROBLEM_JSON),
    APPLICATION_PROBLEM_XML("application/problem+xml", MediaType.APPLICATION_PROBLEM_XML),

    APPLICATION_RSS_XML("application/rss+xml", MediaType.APPLICATION_RSS_XML),
    APPLICATION_NDJSON("application/x-ndjson", MediaType.APPLICATION_NDJSON),
    APPLICATION_XHTML_XML("application/xhtml+xml", MediaType.APPLICATION_XHTML_XML),
    APPLICATION_XML("application/xml", MediaType.APPLICATION_XML),
    IMAGE_GIF("image/gif", MediaType.IMAGE_GIF),
    IMAGE_JPEG("image/jpeg", MediaType.IMAGE_JPEG),
    IMAGE_PNG("image/png", MediaType.IMAGE_PNG),
    MULTIPART_FORM_DATA("multipart/form-data", MediaType.MULTIPART_FORM_DATA),
    MULTIPART_MIXED("multipart/mixed", MediaType.MULTIPART_MIXED),
    MULTIPART_RELATED("multipart/related", MediaType.MULTIPART_RELATED),
    TEXT_EVENT_STREAM("text/event-stream", MediaType.TEXT_EVENT_STREAM),
    TEXT_HTML("text/html", MediaType.TEXT_HTML),
    TEXT_MARKDOWN("text/markdown", MediaType.TEXT_MARKDOWN),
    TEXT_PLAIN("text/plain", MediaType.TEXT_PLAIN),
    APPLICATION_JSON_UTF8("application/json;charset=UTF-8", MediaType.APPLICATION_JSON_UTF8),
    APPLICATION_PROBLEM_JSON_UTF8("application/problem+json;charset=UTF-8", MediaType.APPLICATION_PROBLEM_JSON_UTF8),
    APPLICATION_STREAM_JSON("application/stream+json", MediaType.APPLICATION_STREAM_JSON);

    public final String identity;

    public final MediaType mediaType;

    BlueMediaType(String identity, MediaType mediaType) {
        this.identity = identity;
        this.mediaType = mediaType;
    }
}
