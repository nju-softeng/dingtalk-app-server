package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.ExternalPaper;
import com.softeng.dingtalk.entity.Paper;
import org.springframework.data.jpa.repository.Query;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-21 21:28
 **/
public interface ExternalPaperRepository extends CustomizedRepository<ExternalPaper, Integer> {
    /**
     * 查询对应指定投票id的外部论文
     * @param vid
     * @return
     */
    @Query("select ep from ExternalPaper ep where ep.vote.id = :vid")
    ExternalPaper findByVid(int vid);
}
