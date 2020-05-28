package com.softeng.dingtalk.service;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.Dept;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.repository.DeptDetailRepository;
import com.softeng.dingtalk.repository.DeptRepository;
import com.softeng.dingtalk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhanyeye
 * @description
 * @create 5/27/2020 4:31 PM
 */
@Service
@Transactional
@Slf4j
public class SystemService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DingTalkUtils dingTalkUtils;
    @Autowired
    DeptRepository deptRepository;
    @Autowired
    DeptDetailRepository deptDetailRepository;



    /**
     * 将 userid 变为 uid，前端用 jsapi 选人，若数据库中没有，从数据库中更新
     * @param userid
     * @return
     */
    public int getIdByUserid(String userid) {
        Integer id = userRepository.findIdByUserid(userid);
        if (id == null) {
            User user = addNewUser(userid);
            return user.getId();
        }
        return id;
    }



    /**
     * 从钉钉服务器拉取用户同步到系统
     */
    @CacheEvict(value = "allUser", allEntries = true)
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


    /**
     * 添加新用户
     * @param userid
     * @return
     */
    public User addNewUser(String userid) {
        OapiUserGetResponse response =  dingTalkUtils.fetchUserDetail(userid);

        // 权限
        int authority;
        if (response.getIsBoss()) {
            // 是否为企业的老板
            authority = User.ADMIN_AUTHORITY;
        } else if (response.getIsLeaderInDepts().indexOf("true") != -1) {
            // 是否为管理员
            authority = User.AUDITOR_AUTHORITY;
        } else {
            authority = User.USER_AUTHORITY;
        }

        // 职位
        Position position;
        if (response.getPosition() == null) {
            position = Position.OTHER;
        } else {
            switch (response.getPosition()){
                case "本" : position = Position.UNDERGRADUATE; break;
                case "硕" : position = Position.POSTGRADUATE;  break;
                case "博" : position = Position.DOCTOR;  break;
                default: position = Position.OTHER;  break;
            }
        }


        User u = userRepository.save(new User(response.getUserid(), response.getName(), response.getAvatar(), authority, position));
        return userRepository.refresh(u);
    }



    /**
     * 多条件查询用户信息
     * 单不是依据 name 查询时，进行缓存
     * @param page
     * @param size
     * @param name
     * @param position
     * @return
     */
    @Cacheable(value = "allUser", condition = "#name == ''")
    public Page<User> multiQueryUser(int page, int size, String name, Position position) {
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.notEqual(root.get("authority"), User.ADMIN_AUTHORITY));
                if ("" != name) {
                    // 根据姓名模糊查询
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                if (null != position) {
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


    /**
     * 拉取组织架构
     * todo 待开发
     */
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

}
