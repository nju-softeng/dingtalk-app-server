package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @date 2/25/2020
 */
public interface ProjectRepository  extends JpaRepository<Project, Integer> {


    /**
     * 修改项目标题
     * @param id
     * @param title
     */
    @Modifying
    @Query("update Project set title = :title where id = :id")
    void updateTitle(@Param("id") int id, @Param("title") String title);


    @Query(value = "SELECT p.id, p.title, p.success_cnt, p.cnt, i.begin_time, i.end_time, i.expectedac, i.status FROM (SELECT * FROM project WHERE auditor_id = :aid) p LEFT JOIN iteration i ON  p.cur_iteration = i.id order by p.id desc", nativeQuery = true)
    List<Map<String, Object>> listProjectInfo(@Param("aid") int aid);

//    // 审核人获取进行中的项目
//    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
//    @Query("select p from Project p where p.auditor.id = :aid and p.status = false")
//    List<Project> listUnfinishProjectByAid(@Param("aid") int aid);
//
//
//    // 审核人获取结束的项目
//    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
//    @Query("select p from Project p where p.auditor.id = :aid and p.status = true")
//    List<Project> listfinishProjectByAid(@Param("aid") int aid);
//
//
//    // 用户获取自己参与的项目
//    @Query("select p from Project p left join ProjectDetail pd on p.id = pd.project.id where pd.user.id = :uid")
//    List<Project> listunfinishProject(@Param("uid")int uid);
//
//
//    // 更新项目
//    @Modifying
//    @Query("update Project set name = :name, beginTime = :beginTime, endTime = :endTime where id = :id")
//    void updateProject(@Param("id") int id,@Param("name") String name,@Param("beginTime") LocalDate beginTime,@Param("endTime") LocalDate endTime);
//
//
//    // 根据 pid 集合查询 project
//    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
//    @Query("select p from Project p where p.id in :pids order by p.id desc")
//    List<Project> findAllById(@Param("pids") List<Integer> ids);

}
