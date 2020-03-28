package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.vo.UserVO;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
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
public interface UserRepository extends CustomizedRepository<User, Integer>, JpaSpecificationExecutor<User> {


    // 通过 userid（钉钉用户码） 查找用户 -> 通过用户进入系统时调用API获得的userid查询用户，判断用户是否在系统中，还是新用户
    @Query("select u from User u where u.userid = :userid")
    User findByUserid(@Param("userid")String userid);


    //查找所有的具有审核权限的用户 -> 供用户提交审核申请时选择  //todo 管理员是否要被列入
    @Query(value = "select id, name from user where authority = 1 and is_deleted = 0", nativeQuery = true)
    List<Map<String, Object>> listAuditor();


    // 查询系统中所有用户
    @Query("select new com.softeng.dingtalk.vo.UserVO(u.id, u.name) from User u where is_deleted = 0")
    List<UserVO> listUserVOS();


    // 查询系统中所用户的 userid
    @Query("select u.userid from User u")
    List<String> listAllUserid();


    // 根据userid 查询用户系统 uid
    @Query("select u.id from User u where u.userid = :userid")
    Integer findIdByUserid(@Param("userid") String userid);


    // 查询用户学位
    @Query("select u.position from User u where id = :uid")
    String getUserPosition(@Param("uid") int uid);


//    // 获取用户权限信息
//    @Query(value = "select id, name, avatar, position , authority from user where authority != 2",nativeQuery = true)
//    List<Map<String, Object>> listRole();


    // 更新用户的审核权限
    @Modifying
    @Query("update User set authority = :authority where id = :uid")
    void updateUserRole(@Param("uid")int uid, @Param("authority") int authority);

}
