package com.softeng.dingtalk.service;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.Dept;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.DeptDetailRepository;
import com.softeng.dingtalk.repository.DeptRepository;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    @Autowired
    private DeptRepository deptRepository;
    @Autowired
    private DeptDetailRepository deptDetailRepository;

    // 查询系统可用用户
    public List<UserVO> listUserVO() {
        return userRepository.listUserVOS();
    }

    // 获取用户的userID （获取周报）
    public String getUserid(int id) {
        return userRepository.findById(id).get().getUserid();
    }


    // 通过useID获取用户 -> 通过用户进入系统时调用API获得的userID查询用户，判断用户是否在系统中，还是新用户
    public User getUser(String userid) {
        return userRepository.findByUserid(userid);
    }


    // 查询所有的审核员 -> 供用户提交审核申请时选择
    public Map getAuditorUser() {
        return Map.of("auditorlist", userRepository.listAuditor());
    }


    // 获取用户信息
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
            addNewUser(userid);
        }
    }


    // 添加新用户
    public User addNewUser(String userid) {
        OapiUserGetResponse response =  dingTalkUtils.fetchUserDetail(userid);
        // 权限
        int authority;
        // 职位
        String position;

        if (response.getIsBoss()) { // 是否为企业的老板
            authority = User.ADMIN_AUTHORITY;
        } else if (response.getIsLeaderInDepts().indexOf("true") != -1) { // 是否为管理员
            authority = User.AUDITOR_AUTHORITY;
        } else {
            authority = User.USER_AUTHORITY;
        }
        if (response.getPosition() == null) {
            position = User.OTHER;
        } else {
            switch (response.getPosition()){
                case "本" : position = User.UNDERGRADUATE; break;
                case "硕" : position = User.POSTGRADUATE;  break;
                case "博" : position = User.DOCTOR;  break;
                default: position = User.OTHER;  break;
            }
        }


        User u = userRepository.save(new User(response.getUserid(), response.getName(), response.getAvatar(), authority, position));
        return userRepository.refresh(u);
    }


    // 将 userid 变为 uid，前端用 jsapi 选人，若数据库中没有，从数据库中更新
    public int getIdByUserid(String userid) {
        Integer id = userRepository.findIdByUserid(userid);
        if (id == null) {
            User user = addNewUser(userid);
             return user.getId();
        }
        return id;
    }



    // 更新用户权限
    public void updateRole(int uid, int authority) {
        userRepository.updateUserRole(uid, authority);
    }


    // 拉取组织架构
    public void fetchDeptInfo() {
        List<OapiDepartmentListResponse.Department> departments = dingTalkUtils.fetchDeptInfo();
        List<Dept> depts = new ArrayList<>();
        Set<String> remoteUserids = new HashSet<>();

        // 清空所有部门详情
        deptDetailRepository.deleteAll();

        for (OapiDepartmentListResponse.Department d : departments) {
            depts.add(new Dept(d.getId(), d.getName(), d.getParentid()));
        }
        deptRepository.saveAll(depts);

        // 从钉钉服务器拉取用户同步到系统
        fetchUsers();



        List<String> userids = userRepository.listAllUserid();

    }


    // 多条件查询用户信息
    public Page<User> multiQueryUser(int page, int size, String name, String position) {
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.notEqual(root.get("authority"), User.ADMIN_AUTHORITY));
                if ("" != name) {
                    // 根据姓名模糊查询
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                if ("" != position) {
                    // 根据学位模糊查询
                    predicates.add(criteriaBuilder.equal(root.get("position"), position));
                }
                Predicate[] arr = new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(arr));
            }
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findAll(spec, pageable);
    }

    // 查询用户详情
    public Map getUserDetail(int uid) {
        User u = userRepository.findById(uid).get();
        return Map.of("name", u.getName(), "avatar", u.getAvatar(), "position",u.getPosition());
    }


}
