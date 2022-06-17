package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.Business;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * business dao
 *
 * @author blue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface BusinessMapper {

    int insert(Business record);

    int insertSelective(Business record);

    int updateByPrimaryKey(Business record);

    int updateByPrimaryKeySelective(Business record);

    int deleteByPrimaryKey(Long id);

    Business selectByPrimaryKey(Long id);

    /**
     * select by member id
     *
     * @param memberId
     * @return
     */
    Business selectByMemberId(@Param("memberId") Long memberId);

    /**
     * select by ids
     *
     * @param ids
     * @return
     */
    List<Business> selectByIds(@Param("ids") List<Long> ids);

}