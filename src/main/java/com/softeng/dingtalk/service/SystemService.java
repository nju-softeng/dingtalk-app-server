package com.softeng.dingtalk.service;

import com.dingtalk.api.response.OapiUserGetResponse;
import com.softeng.dingtalk.api.ContactsApi;
import com.softeng.dingtalk.component.AcAlgorithm;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.PaperLevel;
import com.softeng.dingtalk.entity.SubsidyLevel;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.repository.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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
    SubsidyLevelRepository subsidyLevelRepository;
    @Autowired
    PaperLevelRepository paperLevelRepository;
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    AuditService auditService;

    @Autowired
    ContactsApi contactsApi;

    @Autowired
    WeeklyReportService weeklyReportService;
    @Autowired
    AcRecordRepository acRecordRepository;


    /**
     * 根据钉钉 userId 获取系统 uid，前端JSAPI选人
     * @param userId
     * @return
     */
    public int getIdByUserid(String userId) {
        Integer id = userRepository.findIdByUserid(userId);
        return id != null ? id : addNewUser(userId).getId();
    }


    /**
     * 从钉钉服务器拉取所有用户同步到系统
     */
    public void fetchUsers() {
        contactsApi.listDepid().stream()
                .flatMap(depId -> contactsApi.listUserId(depId).stream())
                .forEach(this::addNewUser);
    }


    /**
     * 添加新用户
     * @param userid
     * @return
     */
    public User addNewUser(String userid) {
        OapiUserGetResponse response =  contactsApi.fetchUserDetail(userid);

        // 权限
        int authority;
        if (response.getIsBoss()) {
            // 是否为企业的老板
            authority = User.ADMIN_AUTHORITY;
        } else if (response.getIsLeaderInDepts().contains("true")) {
            // 是否为管理员
            authority = User.AUDITOR_AUTHORITY;
        } else {
            authority = User.NORMAL_AUTHORITY;
        }

        // 职位
        Position position;
        switch (Optional.ofNullable(response.getPosition()).orElse("")){
            case "本": position = Position.UNDERGRADUATE; break;
            case "硕": position = Position.POSTGRADUATE;  break;
            case "博": position = Position.DOCTOR;  break;
            default:   position = Position.OTHER;  break;
        }

        User user = Optional.ofNullable(userRepository.findByUserid(userid))
                .orElse(
                        new User(
                                response.getUserid(),
                                response.getUnionid(),
                                response.getName(),
                                response.getAvatar(),
                                authority,
                                position
                        )
                );
        user.setAvatar(response.getAvatar());
        user.setName(response.getName());
        user.setUnionid(response.getUnionid());
        return userRepository.save(user);
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
    public Page<User> multiQueryUser(int page, int size, String name, String position) {
        Specification<User> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            predicates.add(criteriaBuilder.notEqual(root.get("authority"), User.ADMIN_AUTHORITY));
            if (!"".equals(name)) {
                // 根据姓名模糊查询
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (!"".equals(position)) {
                // 根据学位模糊查询
                Position eposition = Stream.of(Position.values())
                        .filter(c -> c.getTitle().equals(position))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new);
                predicates.add(criteriaBuilder.equal(root.get("position"), eposition));
            }
            Predicate[] arr = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arr));
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


    /**
     * 查询论文AC标准
     * @return
     */
    public List<PaperLevel> listPaperLevel() {
        return paperLevelRepository.findAll();
    }


    /**
     * 更新论文AC标准
     * @param paperLevels
     */
    public void updatePaperLevel(List <PaperLevel> paperLevels) {
        for (PaperLevel pl : paperLevels) {
            paperLevelRepository.updatePaperLevel(pl.getPaperType(), pl.getTotal());
        }
    }


    /**
     * 更新用户信息
     * @param u
     */
    public void updateUserInfo(User u) {
        log.debug(u.toString());
        userRepository.updateUserInfo(u.getId(), u.getStuNum(), u.getPosition(), u.getAuthority());
    }


    /**
     * 停用用户，学生毕业之后它的信息不应该出现在报表中
     * @param uid
     */
    public void disableUser(int uid) {
        userRepository.deleteById(uid);
    }


    /**
     * 恢复用户
     * @param uid
     */
    public void enableUser(int uid) {
        userRepository.enableUser(uid);
    }


    /**
     * 查询停用的用户
     * @return
     */
    public List<User> queryDisableUser() {
        return userRepository.listDisableUser();
    }

    /**
     * 手动重新计算所有可用用户的绩效
     * 当从数据库录入dc数据时，并不会触发更新 dcsummary
     * 所以需要调用该函数刷新 dcsummary
     */
    public void manulUpdatePerformance(int yearmonth) {
        // todo 拿到所有用户可用 uid
        List<Integer> uidlist = userRepository.listUid();
        // todo 根据用户id,和指定年月更新dcsummary
        for (Integer id : uidlist) {
            auditService.updateDcSummary(id, yearmonth, 1);
            auditService.updateDcSummary(id, yearmonth, 2);
            auditService.updateDcSummary(id, yearmonth, 3);
            auditService.updateDcSummary(id, yearmonth, 4);
            auditService.updateDcSummary(id, yearmonth, 5);
        }
    }

    /**
     * 手动指定某天，当天未交周报的硕士博士扣除相应的分数
     */
    public void manulDeductedPointsUnsubmittedWeeklyReport(LocalDate localDate) {
        var start = localDate.atTime(0, 0, 0);
        var end = start.plusDays(1);
        var users = weeklyReportService.queryUnSubmittedWeeklyReportUser(start, end);
        log.info(start.toString() + "未提交周报扣分" + Arrays.toString(users.stream().map(User::getName).toArray()));
        acRecordRepository.saveAll(
                users.stream()
                        .map(user -> AcRecord.builder()
                                .user(user)
                                .ac(AcAlgorithm.getPointOfUnsubmittedWeekReport(user))
                                .classify(AcRecord.NORMAL)
                                .reason(String.format(
                                        "%s 未按时提交周报",
                                        start.toLocalDate().toString()
                                ))
                                .createTime(end)
                                .build())
                        .collect(Collectors.toList())
        );
    }

}
