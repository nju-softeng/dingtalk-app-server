package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.IterationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanyeye
 * @description
 * @date 3/14/2020
 */
public interface IterationDetailRepository extends JpaRepository<IterationDetail, Integer> {

    /**
     * 删除Iteration 的所有 iterationDetail
     * @param id 主键
     **/
    void deleteByIterationId(int id);



}
