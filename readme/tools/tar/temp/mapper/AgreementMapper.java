package temp.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import temp.entity.Agreement;
import temp.entity.AgreementExample;

public interface AgreementMapper {
    long countByExample(AgreementExample example);

    int deleteByExample(AgreementExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Agreement row);

    int insertSelective(Agreement row);

    List<Agreement> selectByExample(AgreementExample example);

    Agreement selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Agreement row, @Param("example") AgreementExample example);

    int updateByExample(@Param("row") Agreement row, @Param("example") AgreementExample example);

    int updateByPrimaryKeySelective(Agreement row);

    int updateByPrimaryKey(Agreement row);
}