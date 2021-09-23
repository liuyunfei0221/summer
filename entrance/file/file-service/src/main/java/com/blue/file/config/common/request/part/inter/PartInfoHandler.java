package com.blue.file.config.common.request.part.inter;

import org.springframework.http.codec.multipart.Part;

import java.util.List;
import java.util.Map;

/**
 * 文件信息处理接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface PartInfoHandler {

    /**
     * 获取可以处理的文件类的集合
     *
     * @return
     */
    List<String> classesCanHandle();

    /**
     * 解析非文件体之外的相关文件信息
     *
     * @param part
     * @return
     */
    Map<String, String> process(Part part);

}
