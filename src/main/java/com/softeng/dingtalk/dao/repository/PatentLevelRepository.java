package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.PatentLevelPo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatentLevelRepository extends CustomizedRepository<PatentLevelPo,Integer>{

    @Query("select pl.total from PatentLevelPo pl where pl.title = :title")
    Double getValue(@Param("title") String title);

    @Modifying
    @Query("update PatentLevelPo set total = :total where title = :title")
    void updatePatentLevel(@Param("title") String title, @Param("total") double total);
}
