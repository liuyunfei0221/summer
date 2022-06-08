package com.blue.base.constant.common;

import org.springframework.http.MediaType;

/**
 * file suffix with media type mapping
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaEnumConstantsMustHaveComment"})
public enum BlueFileType {

    /**
     * .gif
     */
    GIF("gif", MediaType.IMAGE_GIF),

    /**
     * .jpeg
     */
    JPEG("jpeg", MediaType.IMAGE_JPEG),

    /**
     * .jpg
     */
    JPG("jpg", MediaType.IMAGE_JPEG),

    /**
     * .png
     */
    PNG("png", MediaType.IMAGE_PNG),

    /**
     * .pdf
     */
    PDF("pdf", MediaType.APPLICATION_PDF),

    /**
     * .mkv
     */
    MKV("mkv", MediaType.APPLICATION_OCTET_STREAM),

    /**
     * .rmvb
     */
    RMVB("rmvb", MediaType.APPLICATION_OCTET_STREAM);

    public final String identity;

    public final MediaType mediaType;

    BlueFileType(String identity, MediaType mediaType) {
        this.identity = identity;
        this.mediaType = mediaType;
    }
}
