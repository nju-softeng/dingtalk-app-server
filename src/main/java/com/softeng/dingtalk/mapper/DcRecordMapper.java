package com.softeng.dingtalk.mapper;

import com.softeng.dingtalk.vo.DcRecordVO;
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

   @Select("select count(id) from dc_record where applicant_id = #{uid}")
   Integer countDcRecordByuid(int uid);
}

