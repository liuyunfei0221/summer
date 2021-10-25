package com.blue.file.repository.mapper;

import com.blue.file.repository.entity.DownloadHistory;

/**
 * download history mapper
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