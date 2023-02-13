package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.AcItemPo;
import com.softeng.dingtalk.po.DcRecordPo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 12/11/2019
 */
@Repository
public interface AcItemRepository extends CustomizedRepository<AcItemPo, Integer> {
    @Query("select a from AcItemPo a where a.dcRecord.id = :id")
    List<AcItemPo> findAllByDcRecordID(@Param("id") int id);

    /**
     * 更新时先将旧数据删除
     * @param dcRecordPO
     * @return void
     * @Date 4:22 PM 2/1/2020
     **/
    void deleteByDcRecord(DcRecordPo dcRecordPO);

}
