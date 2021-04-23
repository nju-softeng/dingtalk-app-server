package com.softeng.pms.repository;

import com.softeng.pms.entity.VoteDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface VoteDetailRepository extends CustomizedRepository<VoteDetail, Integer> {

    /**
     * 查询参与指定投票的用户id
     * @param vid 所指定的投票
     * @return
     */
    @Query("select vd.user.id from VoteDetail vd where vd.vote.id = :vid")
    Set<Integer> findVoteUserid(int vid);

    /**
     * 获得指定投票的支持人数
     * @param vid
     * @return
     */
    @Query(value = "select count(id) from vote_detail where vote_id = :vid and result = true", nativeQuery = true)
    Integer getAcceptCnt(int vid);

    /**
     * 获得指定投票的总票数
     * @param vid
     * @return
     */
    @Query(value = "select count(id) from vote_detail where vote_id = :vid", nativeQuery = true)
    Integer getCnt(int vid);


    /**
     * 查看用户是否已经投票
     * @param vid
     * @param uid
     * @return
     */
    @Query(value = "SELECT IfNULL((SELECT 1 FROM vote_detail WHERE vote_id = :vid and user_id = :uid  LIMIT 1), 0)", nativeQuery = true)
    Integer isExist(int vid, int uid);


    /**
     * 获取指定用户指定投票的结果
     * @param vid
     * @param uid
     * @return
     */
    @Query("select vd.result from VoteDetail vd where vd.vote.id = :vid and vd.user.id = :uid")
    Boolean getVoteDetail(int vid, int uid);


    /**
     * 获取指定投票的每个人投票结果
     * @param vid
     * @return
     */
    @EntityGraph(value="voteDetail.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select vd from VoteDetail vd where vd.vote.id = :vid")
    List<VoteDetail> listByVid(int vid);


    /**
     * 获取投accept票的人的名单列表
     * @param vid
     * @return
     */
    @Query("select vd.user.name from VoteDetail vd where vd.vote.id = :vid and vd.result = true")
    List<String> listAcceptNamelist(int vid);


    /**
     * 获取投reject票的人的名单列表
     * @param vid
     * @return
     */
    @Query("select vd.user.name from VoteDetail vd where vd.vote.id = :vid and vd.result = false")
    List<String> listRejectNamelist(int vid);


}
