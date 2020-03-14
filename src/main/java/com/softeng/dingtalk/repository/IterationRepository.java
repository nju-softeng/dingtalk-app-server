package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Iteration;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 3/14/2020
 */
public interface IterationRepository extends JpaRepository<Iteration, Integer> {

    // 审核人获取进行中的项目
    @EntityGraph(value="iteration.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select i from Iteration i where i.auditor.id = :aid and i.status = false")
    List<Iteration> listUnfinishIterationByAid(@Param("aid") int aid);


    // 审核人获取结束的项目
    @EntityGraph(value="iteration.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select i from Iteration i where i.auditor.id = :aid and i.status = true")
    List<Iteration> listfinishIterationByAid(@Param("aid") int aid);


    // 用户获取自己参与的项目
    @Query("select p from Iteration p left join IterationDetail pd on p.id = pd.iteration.id where pd.user.id = :uid")
    List<Iteration> listunfinishIteration(@Param("uid")int uid);


    // 更新项目
    @Modifying
    @Query("update Iteration set name = :name, beginTime = :beginTime, endTime = :endTime where id = :id")
    void updateIteration(@Param("id") int id, @Param("name") String name, @Param("beginTime") LocalDate beginTime, @Param("endTime") LocalDate endTime);


    // 根据 pid 集合查询 iteration
    @EntityGraph(value="iteration.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select p from Iteration p where p.id in :pids order by p.id desc")
    List<Iteration> findAllById(@Param("pids") List<Integer> ids);

}
