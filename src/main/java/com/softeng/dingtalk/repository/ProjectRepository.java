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

    // 审核人获取进行中的项目
    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select p from Project p where p.auditor.id = :aid and p.status = false")
    List<Project> listUnfinishProjectByAid(@Param("aid") int aid);


    // 审核人获取结束的项目
    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select p from Project p where p.auditor.id = :aid and p.status = true")
    List<Project> listfinishProjectByAid(@Param("aid") int aid);


    // 用户获取自己参与的项目
    @Query("select p from Project p left join ProjectDetail pd on p.id = pd.project.id where pd.user.id = :uid")
    List<Project> listunfinishProject(@Param("uid")int uid);


}
