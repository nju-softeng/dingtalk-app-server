package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface PaperRepository extends JpaRepository<Paper, Integer> {
    @Modifying
    @Query("update Paper p set p.result = :result where e.id = :id")
    void updatePaperResult(@Param("id") int id, @Param("result")int result);
}
