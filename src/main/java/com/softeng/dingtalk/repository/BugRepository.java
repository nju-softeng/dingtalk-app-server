package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Bug;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 3/12/2020
 */
@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
    List<Bug> findAllByProjectId(int id);

    @EntityGraph(value="bug.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select b from Bug b where b.project.auditor.id = :aid")
    List<Bug> listBugByAuditor(@Param("aid") int aid);
}
