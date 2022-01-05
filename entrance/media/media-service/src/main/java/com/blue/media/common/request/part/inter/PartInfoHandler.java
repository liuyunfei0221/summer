package com.blue.media.common.request.part.inter;

import org.springframework.http.codec.multipart.Part;

import java.util.List;
import java.util.Map;

/**
 * part handler interface
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface PartInfoHandler {

    /**
     * witch part types can handle
     *
     * @return
     */
    List<String> classesCanHandle();

    /**
     * parse part info exclude body bytes
     *
     * @param part
     * @return
     */
    Map<String, String> process(Part part);

}
