package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Project;
import com.softeng.dingtalk.entity.ProjectDetail;
import com.softeng.dingtalk.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Integer> {



////    @EntityGraph(value="project.graph",type= EntityGraph.EntityGraphType.FETCH)
//    @Query("select pd.project from ProjectDetail pd where pd.user.id = :uid")
//    List<Project> listProjectByUid(@Param("uid") int uid);

//    // 查询开发者参与的项目id
//    @Query("select pd.project.id from ProjectDetail pd where pd.user.id = :uid")
//    List<Integer> listProjectIdByUid(@Param("uid") int uid);
//
//
//    // 删除指定project 的分配信息
//    void deleteByProjectId(int id);
//
//
//    // 查询项目的分配细节
//    List<ProjectDetail> findAllByProject(Project project);
//
//
//    // 查询用户id
//    @Query("select pd.user from ProjectDetail pd where pd.project.id = :pid")
//    List<User> findUserByProjectId(@Param("pid") int pid);
//
//    // 查询项目期间的dc
//    @Query(value = "SELECT u.name, d.dc, CONCAT(d.yearmonth, '-',d.`week`) as 'date'" +
//           "FROM project_detail AS pd INNER JOIN `user` AS u ON pd.project_id = :pid AND pd.user_id = u.id " +
//            "INNER JOIN dc_record AS d ON u.id = d.applicant_id AND d.auditor_id = :aid AND d.weekdate >= :stime AND d.weekdate <= :etime", nativeQuery = true)
//    List<Map<String, String>> getProjectDc(@Param("pid") int pid, @Param("aid") int aid, @Param("stime") LocalDate stime, @Param("etime") LocalDate etime);

}
