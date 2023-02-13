package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.ProjectPo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @date 2/25/2020
 */
@Repository
public interface ProjectRepository  extends CustomizedRepository<ProjectPo, Integer> {

    /**
     * 修改项目标题
     * @param id
     * @param title
     */
    @Modifying
    @Query("update ProjectPo set title = :title where id = :id")
    void updateTitle(@Param("id") int id, @Param("title") String title);


    @Query(value = "SELECT p.id, p.title, p.success_cnt, p.cnt, i.id as itid, i.begin_time, i.end_time, i.finish_time, i.expectedac, i.status FROM (SELECT * FROM project WHERE auditor_id = :aid) p LEFT JOIN iteration i ON  p.cur_iteration = i.id order by p.id desc", nativeQuery = true)
    List<Map<String, Object>> listProjectInfo(@Param("aid") int aid);


    /**
     * 查询所有项目
     * @return
     */
    @Query(value = "SELECT p.id, p.title, p.success_cnt, p.cnt, u.name, i.id as itid, i.begin_time, i.end_time, i.finish_time, i.expectedac, i.status FROM project p LEFT JOIN iteration i ON  p.cur_iteration = i.id LEFT JOIN user u on p.auditor_id = u.id order by p.id desc", nativeQuery = true)
    List<Map<String, Object>> listAllProjectInfo();

}
