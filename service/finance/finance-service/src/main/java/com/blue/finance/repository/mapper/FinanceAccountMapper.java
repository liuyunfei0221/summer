package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * finance account dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface FinanceAccountMapper {

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int deleteByPrimaryKey(Long id);

    FinanceAccount selectByPrimaryKey(Long id);

    FinanceAccount getByMemberId(@Param("memberId") Long memberId);

    List<FinanceAccount> selectByIds(@Param("ids") List<Long> ids);
}