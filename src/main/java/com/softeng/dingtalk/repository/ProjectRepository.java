package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @date 2/25/2020
 */
public interface ProjectRepository  extends JpaRepository<Project, Integer> {

    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select p from Project p where p.auditor.id = :aid and p.status = false")
    List<Project> listUnfinishProjectByAid(@Param("aid") int aid);

    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select p from Project p where p.auditor.id = :aid and p.status = true")
    List<Project> listfinishProjectByAid(@Param("aid") int aid);
}
