package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.ExternalPaper;
import com.softeng.dingtalk.po_entity.Vote;
import com.softeng.dingtalk.vo.PaperFileInfoVO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 查询指定论文的投票
     * @param id
     * @return
     */
    @Query("select ep.vote from ExternalPaper ep where ep.id = :id")
    Vote findVoteById(int id);

    /**
     * 查询外部评审论文文件信息
     * @param id
     * @return
     */
    @Query("select new com.softeng.dingtalk.vo.PaperFileInfoVO(p.id, p.reviewFileName, p.reviewFileId,p.submissionFileName," +
            "p.submissionFileId,p.publishedFileName,p.publishedFileId,p.publishedLatexFileName,p.publishedLatexFileId," +
            "p.publicFileName,p.publicFileId,p.sourceFileName,p.sourceFileId,p.commentFileName,p.commentFileId) " +
            "from ExternalPaper  p where p.id= :id")
    PaperFileInfoVO getExternalPaperFileInfo(@Param("id") int id);
}
