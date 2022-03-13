package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.InternalPaper;
import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.vo.PaperFileInfoVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface InternalPaperRepository extends CustomizedRepository<InternalPaper, Integer> {

    /**
     * 根据id 获取论文title
     *
     * @param id
     * @return
     */
    @Query("select p.title from InternalPaper p where p.id = :id")
    String getPaperTitleById(@Param("id") int id);

    /**
     * 获取指定投票对应的论文id和标题
     *
     * @param vid
     * @return
     */
    @Query(value = "select p.id, p.title from internal_paper p where p.vote_id = :vid", nativeQuery = true)
    Map<String, Object> getPaperInfo(@Param("vid") int vid);

    /**
     * 查询对应指定投票id的论文
     *
     * @param vid
     * @return
     */
    @Query("select p from InternalPaper p where p.vote.id = :vid")
    InternalPaper findByVid(int vid);

    /**
     * 发起论文投票时，更新论文记录
     *
     * @param id
     * @param vid
     */
    @Modifying
    @Query(value = "update internal_paper set vote_id = :vid where id = :id", nativeQuery = true)
    void updatePaperVote(@Param("id") int id, @Param("vid") int vid);

    /**
     * 查询指定论文的投票id
     *
     * @param id
     * @return
     */
    @Query("select p.vote.id from InternalPaper p where p.id = :id")
    Integer findVidById(@Param("id") int id);

    /**
     * 查询指定论文的投票
     *
     * @param id
     * @return
     */
    @Query("select p.vote from InternalPaper p where p.id = :id")
    Vote findVoteById(@Param("id") int id);


    /**
     * 更新论文的投稿状态
     *
     * @param id
     * @param result
     */
    @Modifying
    @Query("update InternalPaper set result = :result where id = :id")
    void updatePaperResult(int id, int result);

    @Query("select new com.softeng.dingtalk.vo.PaperFileInfoVO(p.id, p.reviewFileName, p.reviewFileId,p.submissionFileName," +
            "p.submissionFileId,p.publishedFileName,p.publishedFileId,p.publishedLatexFileName,p.publishedLatexFileId) " +
            "from InternalPaper  p where p.id= :id")
    PaperFileInfoVO getPaperFileInfo(@Param("id") int id);

}
