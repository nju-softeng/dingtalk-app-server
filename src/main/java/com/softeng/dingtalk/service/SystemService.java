package com.softeng.dingtalk.service;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.Dept;
import com.softeng.dingtalk.entity.SubsidyLevel;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.repository.DeptDetailRepository;
import com.softeng.dingtalk.repository.DeptRepository;
import com.softeng.dingtalk.repository.SubsidyLevelRepository;
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
import java.util.stream.Stream;

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
    SubsidyLevelRepository subsidyLevelRepository;


    /**
     * 根据钉钉 userid 获取系统 uid，前端JSAPI选人
     * @param userid
     * @return
     */
    public int getIdByUserid(String userid) {
        Integer id = userRepository.findIdByUserid(userid);
        if (id == null) {
            // 如果该用户不在系统中，重新拉取
            User user = addNewUser(userid);
            return user.getId();
        }
        return id;
    }


    /**
     * 从钉钉服务器拉取所有用户同步到系统
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
                    Position eposition = Stream.of(Position.values())
                            .filter(c -> c.getTitle().equals(position))
                            .findFirst()
                            .orElseThrow(IllegalArgumentException::new);
                    predicates.add(criteriaBuilder.equal(root.get("position"), eposition));
                }
                Predicate[] arr = new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(arr));
            }
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findAll(spec, pageable);
    }


    /**
     * 查询绩效标准
     * @param position
     * @return double
     */
    @Cacheable(value="subsidy", key = "#position")
    public double getSubsidy(Position position) {
        return subsidyLevelRepository.getSubsidy(position);
    }


    /**
     * 更新绩效标准
     * @param subsidyLevelList
     */
    @CacheEvict(value = "subsidy", allEntries = true)
    public void setSubsidy(List<SubsidyLevel> subsidyLevelList) {
        for (SubsidyLevel sl : subsidyLevelList) {
            subsidyLevelRepository.updateSubsidy(sl.getPosition(), sl.getSubsidy());
        }
    }


    /**
     * 获取所有的绩效标准
     * @return List<SubsidyLevel>
     */
    public List<SubsidyLevel> listSubsidy() {
        return subsidyLevelRepository.findAll();
    }



}
