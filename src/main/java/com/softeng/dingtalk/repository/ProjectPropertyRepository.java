package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Project;
import com.softeng.dingtalk.entity.ProjectProperty;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPropertyRepository extends CustomizedRepository<ProjectProperty, Integer>{
}
