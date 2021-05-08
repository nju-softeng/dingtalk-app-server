package com.softeng.dingtalk.mapper;

import com.softeng.dingtalk.vo.DcRecordVO;
import com.softeng.dingtalk.vo.UserVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 07/07/2020
 */
@Repository
public interface DcRecordMapper {
   /**
    * 分页查询
    * @param offset
    * @param size
    * @return
    */
   List<DcRecordVO> listDcRecordVO(int uid, int offset, int size);

   /**
    * 获取提交的总申请数
    * @param uid
    * @return
    */
   @Select("select count(id) from dc_record where applicant_id = #{uid}")
   Integer countDcRecordByuid(int uid);

   /**
    * 查询申请人最近一次绩效申请的审核人是谁
    * @param uid 申请人id
    * @return
    */
   UserVO findLatestAuditorByApplicantId(int uid);
}

