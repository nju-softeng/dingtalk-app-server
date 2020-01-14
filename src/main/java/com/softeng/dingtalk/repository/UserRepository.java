package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description 操作User实体类的接口
 * @date 12/7/2019
 */
@Repository
public interface UserRepository extends CustomizedRepository<User, Integer>, JpaRepository<User, Integer> {
    /**
     * 通过 userid（钉钉用户码） 查找用户 -> 通过用户进入系统时调用API获得的userid查询用户，判断用户是否在系统中，还是新用户
     * @param userid 钉钉用户码
     * @return com.softeng.dingtalk.entity.User
     * @Date 9:10 PM 1/10/2020
     **/
    @Query("select u from User u where u.userid = :userid")
    User findByUserid(@Param("userid")String userid);


    //todo 管理员是否要被列入
    //查找所有的具有审核权限的用户 -> 供用户提交审核申请时选择
    @Query(value = "select id, name from user where authority = 1", nativeQuery = true)
    List<Map<String, Object>> listAuditor();


}
