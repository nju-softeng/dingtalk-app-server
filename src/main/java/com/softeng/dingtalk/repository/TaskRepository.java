package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description
 * @date 1/3/2020
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

}
