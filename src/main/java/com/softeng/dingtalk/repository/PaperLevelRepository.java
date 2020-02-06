package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.PaperLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author zhanyeye
 * @description
 * @date 2/6/2020
 */
public interface PaperLevelRepository extends JpaRepository<PaperLevel, Integer> {
    @Query("select pl.total from PaperLevel pl where pl.level = :level")
    Double getvalue(@Param("level") int level);
}
