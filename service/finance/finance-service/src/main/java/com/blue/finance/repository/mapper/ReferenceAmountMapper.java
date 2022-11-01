package com.blue.finance.repository.mapper;

import com.blue.finance.model.db.ReferenceAmountUpdateModel;
import com.blue.finance.repository.entity.ReferenceAmount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * reference amount dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface ReferenceAmountMapper {

    int insert(ReferenceAmount record);

    int insertBatch(@Param("list") List<ReferenceAmount> list);

    int insertSelective(ReferenceAmount record);

    int updateByPrimaryKey(ReferenceAmount record);

    int updateByPrimaryKeySelective(ReferenceAmount record);

    int deleteByPrimaryKey(Long id);

    ReferenceAmount selectByPrimaryKey(Long id);

    List<ReferenceAmount> getByOrderId(@Param("orderId") Long orderId);

    List<ReferenceAmount> selectByIds(@Param("ids") List<Long> ids);

    int updateTargetColumnByPrimaryKeySelectiveWithStatusStamp(@Param("referenceAmountUpdateModel") ReferenceAmountUpdateModel referenceAmountUpdateModel);

}