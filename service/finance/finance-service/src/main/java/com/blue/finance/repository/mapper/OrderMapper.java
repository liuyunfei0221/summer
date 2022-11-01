package com.blue.finance.repository.mapper;

import com.blue.finance.model.db.OrderUpdateModel;
import com.blue.finance.model.db.OrderVersionUpdateModel;
import com.blue.finance.repository.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * order dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface OrderMapper {

    int insert(Order record);

    int insertSelective(Order record);

    int updateByPrimaryKey(Order record);

    int updateByPrimaryKeySelective(Order record);

    int deleteByPrimaryKey(Long id);

    Order selectByPrimaryKey(Long id);

    List<Order> selectByIds(@Param("ids") List<Long> ids);

    int updateTargetColumnByPrimaryKeySelectiveWithStamps(@Param("orderUpdateModel") OrderUpdateModel orderUpdateModel);

    int updateVersionByPrimaryKeyWithVersionStamp(@Param("orderVersionUpdateModel") OrderVersionUpdateModel orderVersionUpdateModel);

}