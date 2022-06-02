package com.blue.base.repository.mapper;

import com.blue.base.model.StyleCondition;
import com.blue.base.repository.entity.Style;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * style dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface StyleMapper {

    int insert(Style record);

    int insertSelective(Style record);

    int updateByPrimaryKey(Style record);

    int updateByPrimaryKeySelective(Style record);

    int deleteByPrimaryKey(Long id);

    Style selectByPrimaryKey(Long id);

    List<Style> select();

    List<Style> selectByIds(@Param("ids") List<Long> ids);

    List<Style> selectByTypeAndActive(@Param("type") Integer type, @Param("isActive") Boolean isActive);

    List<Style> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("styleCondition") StyleCondition styleCondition);

    Long countByCondition(@Param("styleCondition") StyleCondition styleCondition);

    Style selectByName(@Param("name") String name);

}