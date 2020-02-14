package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.PaperDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface PaperDetailRepository extends JpaRepository<PaperDetail, Integer> {
    void deleteByPaper(Paper paper);
    List<PaperDetail> findByPaper(Paper paper);


    //更具 paperid 删除
    @Modifying
    @Query("delete from PaperDetail pd where pd.paper.id = :id")
    void deleteByPaperid(@Param("id") int id);


}
