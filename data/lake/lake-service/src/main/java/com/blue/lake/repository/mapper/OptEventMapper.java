package com.blue.lake.repository.mapper;

import com.blue.lake.repository.entity.OptEvent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * option event dao
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface OptEventMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OptEvent record);

    void insertBatch(@Param("list") List<OptEvent> list);

    int insertSelective(OptEvent record);

    OptEvent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OptEvent record);

    int updateByPrimaryKey(OptEvent record);

    List<OptEvent> selectByLimitAndRows(@Param("limit") long limit, @Param("rows") long rows);

}