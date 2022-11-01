package com.blue.finance.repository.mapper;

import com.blue.finance.model.db.OrderArticleUpdateModel;
import com.blue.finance.repository.entity.OrderArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * order article dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface OrderArticleMapper {

    int insert(OrderArticle record);

    int insertBatch(@Param("list") List<OrderArticle> list);

    int insertSelective(OrderArticle record);

    int updateByPrimaryKey(OrderArticle record);

    int updateByPrimaryKeySelective(OrderArticle record);

    int deleteByPrimaryKey(Long id);

    OrderArticle selectByPrimaryKey(Long id);

    List<OrderArticle> getByOrderId(@Param("orderId") Long orderId);

    List<OrderArticle> selectByIds(@Param("ids") List<Long> ids);

    int updateTargetColumnByPrimaryKeySelectiveWithStatusStamp(@Param("orderArticleUpdateModel") OrderArticleUpdateModel orderArticleUpdateModel);

}