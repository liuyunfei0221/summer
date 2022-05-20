package com.blue.media.common.part.impl;

import com.blue.media.common.part.inter.PartInfoHandler;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.blue.media.common.part.constant.FilePartElementKey.*;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;


/**
 * handle AbstractSyncPart
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class FormFieldPartInfoHandler implements PartInfoHandler {

    private static final List<String> HANDLE_CLASSES = Stream.of(
            "org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader$SynchronossFormFieldPart"
    ).collect(toList());

    @Override
    public List<String> classesCanHandle() {
        return HANDLE_CLASSES;
    }

    @Override
    public Map<String, String> process(Part part) {

        FormFieldPart formFieldPart = (FormFieldPart) part;
        Map<String, String> info = new HashMap<>(8);

        info.put(PART_CLASS.identity, part.getClass().getName());
        info.put(PART_NAME.identity, formFieldPart.name());
        info.put(FILE_NAME.identity, formFieldPart.value());
        info.put(PART_HEADERS.identity, of(part.headers())
                .stream()
                .flatMap(headers -> headers.entrySet().stream())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(toList()).toString());

        return info;
    }
}
