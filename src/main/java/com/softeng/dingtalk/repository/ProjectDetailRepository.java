package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Project;
import com.softeng.dingtalk.entity.ProjectDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 2/25/2020
 */
public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Integer> {

//    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select pd.project from ProjectDetail pd where pd.user.id = :uid")
    List<Project> listProjectByUid(@Param("uid") int uid);

    // 删除指定project 的分配信息
    void deleteByProjectId(int id);


    List<ProjectDetail> findAllByProject(Project project);
}
