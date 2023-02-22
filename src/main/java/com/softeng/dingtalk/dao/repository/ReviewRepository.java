package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 6/6/2020
 */
@Repository
public interface ReviewRepository  extends CustomizedRepository<Review, Integer> {

    /**
     * 根据论文类型和论文id,查询论文对应的评审意见
     * @param pid
     * @param isExternal
     * @return
     */
    @Query("select r from Review r where r.paperid = :pid and r.external = :external order by r.id DESC")
    List<Review> findAllByPaperidAndExternal(int pid, boolean external);

    void deleteByPaperid(int pid);

}
