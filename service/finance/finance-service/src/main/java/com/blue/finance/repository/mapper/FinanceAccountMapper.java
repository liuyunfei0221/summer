package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.FinanceAccount;
import org.apache.ibatis.annotations.Param;

/**
 * 用户资金账户信息持久层
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
     * 根据成员手机号查询成员资金账户信息
     *
     * @param memberId
     * @return
     */
    FinanceAccount selectByMemberId(@Param("memberId") Long memberId);

}