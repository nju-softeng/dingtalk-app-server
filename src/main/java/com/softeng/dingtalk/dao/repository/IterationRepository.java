package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.Iteration;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 3/14/2020
 */
public interface IterationRepository extends CustomizedRepository<Iteration, Integer> {

    /**
     * 查询项目的所有迭代
     * @param pid
     * @return
     */
    @EntityGraph(value="iteration.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select it from Iteration it where it.project.id = :pid order by it.id desc")
    List<Iteration> listIterationByPid(@Param("pid") int pid);

    /**
     * 根据id查询当前迭代的连续交付次数
     * @param id
     * @return
     */
    @Query("select i.conSuccess from Iteration i where i.id = :id")
    Integer getConSucessCntById(@Param("id") int id);

    /**
     * 根据id查询迭代
     * @param id
     * @return
     */
    @EntityGraph(value="iteration.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select i from Iteration i where i.id = :id")
    Iteration getIterationById(@Param("id") int id);


    /**
     * 用户获取自己参与的迭代
     * @param uid
     * @return
     */
    @Query("select p from Iteration p left join IterationDetail pd on p.id = pd.iteration.id where pd.user.id = :uid")
    List<Iteration> listunfinishIteration(@Param("uid")int uid);


    /**
     * 根据 id 集合查询 iteration
     * @param ids
     * @return
     */
    @EntityGraph(value="iteration.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select i from Iteration i where i.id in :ids order by i.id desc")
    List<Iteration> findAllById(@Param("ids") List<Integer> ids);


}
