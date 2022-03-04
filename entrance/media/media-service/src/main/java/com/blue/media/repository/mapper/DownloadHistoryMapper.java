package com.blue.media.repository.mapper;

import com.blue.media.repository.entity.DownloadHistory;

/**
 * download history dao
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface DownloadHistoryMapper {

    int insert(DownloadHistory record);

    int insertSelective(DownloadHistory record);

    int updateByPrimaryKey(DownloadHistory record);

    int updateByPrimaryKeySelective(DownloadHistory record);

    int deleteByPrimaryKey(Long id);

    DownloadHistory selectByPrimaryKey(Long id);

}