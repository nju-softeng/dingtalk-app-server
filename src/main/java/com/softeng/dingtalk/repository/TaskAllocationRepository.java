package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Task;
import com.softeng.dingtalk.entity.TaskAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 1/4/2020
 */
@Repository
public interface TaskAllocationRepository extends JpaRepository<TaskAllocation, Integer> {
    void deleteByTask(Task task);

    List<TaskAllocation> findAllByTask(Task task);
}
