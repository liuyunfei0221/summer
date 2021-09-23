package com.blue.file.repository.mapper;

import com.blue.file.repository.entity.DownloadHistory;

/**
 * 下载记录持久层
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface DownloadHistoryMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DownloadHistory record);

    int insertSelective(DownloadHistory record);

    DownloadHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DownloadHistory record);

    int updateByPrimaryKey(DownloadHistory record);

}