package com.blue.verify.repository.mapper;

import com.blue.verify.repository.entity.DownloadHistory;

/**
 * download history mapper
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