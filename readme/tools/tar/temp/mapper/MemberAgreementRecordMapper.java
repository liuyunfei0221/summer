package temp.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import temp.entity.MemberAgreementRecord;
import temp.entity.MemberAgreementRecordExample;

public interface MemberAgreementRecordMapper {
    long countByExample(MemberAgreementRecordExample example);

    int deleteByExample(MemberAgreementRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MemberAgreementRecord row);

    int insertSelective(MemberAgreementRecord row);

    List<MemberAgreementRecord> selectByExample(MemberAgreementRecordExample example);

    MemberAgreementRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") MemberAgreementRecord row, @Param("example") MemberAgreementRecordExample example);

    int updateByExample(@Param("row") MemberAgreementRecord row, @Param("example") MemberAgreementRecordExample example);

    int updateByPrimaryKeySelective(MemberAgreementRecord row);

    int updateByPrimaryKey(MemberAgreementRecord row);
}