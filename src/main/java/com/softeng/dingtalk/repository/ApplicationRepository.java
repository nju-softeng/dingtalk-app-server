package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Application;
import com.softeng.dingtalk.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 12/11/2019
 */
@Repository
public interface ApplicationRepository extends CustomizedRepository<Application, Integer>, JpaRepository<Application, Integer> {
    @Query("select a from Application a where a.applicant.id = :uid")
    List<Application> listApplicationByuid(@Param("uid") int uid, Pageable pageable);

}
