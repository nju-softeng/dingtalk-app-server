package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.PatentLevel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatentLevelRepository extends CustomizedRepository<PatentLevel,Integer>{

    @Query("select pl.total from PatentLevel pl where pl.title = :title")
    Double getValue(@Param("title") String title);

    @Modifying
    @Query("update PatentLevel set total = :total where title = :title")
    void updatePatentLevel(@Param("title") String title, @Param("total") double total);
}
