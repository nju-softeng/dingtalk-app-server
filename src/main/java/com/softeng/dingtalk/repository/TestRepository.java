package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Test;
import com.softeng.dingtalk.enums.Position;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zhanyeye
 * @description
 * @date 5/28/2020
 */
public interface TestRepository extends CustomizedRepository<Test, Integer> {
    @Query("select t.position from Test t where t.id = 1")
    Position test();

}
