package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zhanyeye
 * @description User业务逻辑组件
 * @date 12/7/2019
 */
@Service
@Transactional
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AcRecordRepository acRecordRepository;
    @Autowired
    private DingTalkUtils dingTalkUtils;

    public List<UserVO> listUserVO() {
        return userRepository.listUserVOS();
    }

    //获取用户的userID -> 从钉钉API获取用户信息时，要根据userID获取  （获取周报）
    public String getUserid(int id) {
        return userRepository.findById(id).get().getUserid();
    }

    //通过useID获取用户 -> 通过用户进入系统时调用API获得的userID查询用户，判断用户是否在系统中，还是新用户
    public User getUser(String userid) {
        return userRepository.findByUserid(userid);
    }

    //添加用户 -> 用于新用户登录时，将其添加到数据库中
    public User addUser(User user) {
        User u = userRepository.save(user);
        return userRepository.refresh(u);
    }

    //查询所有的审核员 -> 供用户提交审核申请时选择
    public Map getAuditorUser() {
        return Map.of("auditorlist", userRepository.listAuditor());
    }

    /**
     * 获取用户信息
     * @param uid
     * @return java.util.Map
     * @Date 9:18 PM 1/10/2020
     **/
    public Map getUserInfo(int uid) {
        User u = userRepository.findById(uid).get();
        double ac = acRecordRepository.getUserAcSum(uid);
        dingTalkUtils.getJsapiTicket(); // 提前拿到jsapi ticket，避免需要时再去那减少时延
        if (u.getAvatar() != null) {
            return Map.of("name", u.getName(), "avatar", u.getAvatar(), "ac",ac);
        } else {
            return Map.of("name", u.getName(), "ac",ac);
        }
    }

    // 从钉钉服务器拉取用户同步到系统
    public void fetchUsers() {
        List<String> depids = dingTalkUtils.listDepid();
        Set<String> remoteUserids = new HashSet<>();

        for (String depid : depids) {
            remoteUserids.addAll(dingTalkUtils.listUserId(depid));
        }

        List<String> localUserids = userRepository.listAllUserid();
        remoteUserids.removeAll(localUserids);
        List<User> users = new ArrayList<>();
        for (String userid : remoteUserids) {
            users.add(dingTalkUtils.getNewUser(userid));
        }
        userRepository.saveAll(users);
    }

     // 前端用 jsapi 选人，将 userid 变为 uid，若数据库中没有，从数据库中更新
     public int getIdByUserid(String userid) {
        Integer id = userRepository.findIdByUserid(userid);
        if (id == null) {
            User user = dingTalkUtils.getNewUser(userid);
             user = addUser(user);
             return user.getId();
        }
        return id;
     }

}
