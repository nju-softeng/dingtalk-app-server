package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.PaperDetail;
import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.vo.VoteResultVO;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface VoteDetailRepository extends JpaRepository<VoteDetail, Integer> {

    @Query(value = "select count(id) from vote_detail where vote_id = :vid and result = true", nativeQuery = true)
    Integer getAcceptCnt(@Param("vid") int vid);

    @Query(value = "select count(id) from vote_detail where vote_id = :vid", nativeQuery = true)
    Integer getCnt(@Param("vid")int vid);

    // 查看用户是否已经投票
    @Query(value = "SELECT IfNULL((SELECT 1 FROM vote_detail WHERE vote_id = :vid and user_id = :uid  LIMIT 1), 0)", nativeQuery = true)
    Integer isExist(@Param("vid") int vid, @Param("uid") int uid);


    // 获取指定用户指定投票的结果
    @Query("select vd.result from VoteDetail vd where vd.vote.id = :vid and vd.user.id = :uid")
    Boolean getVoteDetail(@Param("vid") int vid, @Param("uid") int uid);

    // 获取指定投票的每个人投票结果
    @Query("select vd from VoteDetail vd where vd.vote.id = :vid")
    List<VoteDetail> listByVid(@Param("vid") int vid);

    // 获取投accept票的人的名单列表
    @Query("select vd.user.name from VoteDetail vd where vd.vote.id = :vid and vd.result = true")
    List<String> listAcceptNamelist(@Param("vid") int vid);

    // 获取投reject票的人的名单列表
    @Query("select vd.user.name from VoteDetail vd where vd.vote.id = :vid and vd.result = false")
    List<String> listRejectNamelist(@Param("vid") int vid);


}
