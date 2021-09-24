package com.blue.file.common.request.part.impl;

import com.blue.file.common.request.part.inter.PartInfoHandler;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.blue.file.common.request.part.common.FilePartElementKey.*;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;


/**
 * 针对AbstractSynchronossPart及其子类的处理
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
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
        Map<String, String> infos = new HashMap<>(8);

        infos.put(PART_CLASS.identity, part.getClass().getName());
        infos.put(PART_NAME.identity, formFieldPart.name());
        infos.put(FILE_NAME.identity, formFieldPart.value());
        infos.put(PART_HEADERS.identity, of(part.headers())
                .stream()
                .flatMap(headers -> headers.entrySet().stream())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(toList()).toString());

        return infos;
    }
}
