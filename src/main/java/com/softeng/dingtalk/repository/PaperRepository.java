package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface PaperRepository extends CustomizedRepository<Paper, Integer> {

    /**
     * 根据id 获取论文title
     * @param id
     * @return
     */
    @Query("select p.title from Paper p where p.id = :id")
    String getPaperTitleById(@Param("id") int id);

    /**
     * 获取指定投票对应的论文id和标题
     * @param vid
     * @return
     */
    @Query(value = "select p.id, p.title from paper p where p.vote_id = :vid", nativeQuery = true)
    Map<String, Object> getPaperInfo(@Param("vid") int vid);

    /**
     * 查询对应指定投票id的论文
     * @param vid
     * @return
     */
    @Query("select p from Paper p where p.vote.id = :vid")
    Paper findByVid(int vid);

    /**
     * 发起论文投票时，更新论文记录
     * @param id
     * @param vid
     */
    @Modifying
    @Query(value = "update paper set vote_id = :vid where id = :id", nativeQuery = true)
    void updatePaperVote(@Param("id") int id, @Param("vid") int vid);

    /**
     * 查询指定论文的投票id
     * @param id
     * @return
     */
    @Query("select p.vote.id from Paper p where p.id = :id")
    Integer findVidById(@Param("id") int id);

    /**
     * 查询指定论文的投票
     * @param id
     * @return
     */
    @Query("select p.vote from  Paper p where p.id = :id")
    Vote findVoteById(@Param("id") int id);


    /**
     * 更新论文的投稿状态
     * @param id
     * @param result
     */
    @Modifying
    @Query("update Paper set result = :result where id = :id")
    void updatePaperResult(int id, int result);




}
