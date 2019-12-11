package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description
 * @date 12/11/2019
 */
@Repository
public interface ApplicationRepository extends CustomizedRepository<Application, Integer>, JpaRepository<Application, Integer> {

}
