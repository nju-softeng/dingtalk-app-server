package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.PaperLevel;
import com.softeng.dingtalk.enums.PaperType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author zhanyeye
 * @description
 * @date 2/6/2020
 */
public interface PaperLevelRepository extends CustomizedRepository<PaperLevel, Integer> {
    /**
     * 查询指定论文可获得AC值
     * @param papertype
     * @return
     */
    @Query("select pl.total from PaperLevel pl where pl.paperType = :papertype")
    Double getValue(@Param("papertype") PaperType papertype);

    /**
     * 更新论文获得的AC标准
     * @param paperType
     * @param total
     */
    @Modifying
    @Query("update PaperLevel set total = :total where paperType = :papertype")
    void updatePaperLevel(@Param("papertype") PaperType paperType, @Param("total") double total);



}
