package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
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
public interface AcItemRepository extends CustomizedRepository<AcItem, Integer> {
    @Query("select a from AcItem a where a.dcRecord.id = :id")
    List<AcItem> findAllByDcRecordID(@Param("id") int id);

    /**
     * 更新时先将旧数据删除
     * @param dcRecord
     * @return void
     * @Date 4:22 PM 2/1/2020
     **/
    void deleteByDcRecord(DcRecord dcRecord);

}
