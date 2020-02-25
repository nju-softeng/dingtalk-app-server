package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanyeye
 * @description
 * @date 2/25/2020
 */
public interface ProjectRepository  extends JpaRepository<Project, Integer> {

}
