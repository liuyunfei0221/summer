package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.FinanceAccount;
import org.apache.ibatis.annotations.Param;

/**
 * finance account mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused", "JavaDoc"})
public interface FinanceAccountMapper {

    int deleteByPrimaryKey(Long id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    /**
     * get finance account by member id
     *
     * @param memberId
     * @return
     */
    FinanceAccount getByMemberId(@Param("memberId") Long memberId);

}