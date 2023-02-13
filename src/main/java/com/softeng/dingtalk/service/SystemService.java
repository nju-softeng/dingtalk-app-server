package com.softeng.dingtalk.service;

import com.dingtalk.api.response.OapiUserGetResponse;
import com.softeng.dingtalk.api.*;
import com.softeng.dingtalk.component.AcAlgorithm;
import com.softeng.dingtalk.constant.LocalUrlConstant;
import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.po.*;
import com.softeng.dingtalk.enums.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
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
    DingTalkScheduleRepository dingTalkScheduleRepository;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    AuditService auditService;

    @Autowired
    ContactsApi contactsApi;
    @Autowired
    ReportApi reportApi;
    @Autowired
    MessageApi messageApi;
    @Autowired
    ScheduleApi scheduleApi;
    @Autowired
    OAApi oaApi;

    @Autowired
    WeeklyReportService weeklyReportService;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    PatentLevelRepository patentLevelRepository;

    @Value("${DingTalkSchedule.absentACPunishment}")
    double absentACPunishment;

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
    public UserPo addNewUser(String userid) {
        OapiUserGetResponse response =  contactsApi.fetchUserDetail(userid);

        // 权限
        int authority;
        if (response.getIsBoss()) {
            // 是否为企业的老板
            authority = UserPo.ADMIN_AUTHORITY;
        } else if (response.getIsLeaderInDepts().contains("true")) {
            // 是否为管理员
            authority = UserPo.AUDITOR_AUTHORITY;
        } else {
            authority = UserPo.NORMAL_AUTHORITY;
        }

        // 职位
        Position position;
        switch (Optional.ofNullable(response.getPosition()).orElse("")){
            case "本": position = Position.UNDERGRADUATE; break;
            case "学硕": position = Position.ACADEMIC;  break;
            case "专硕": position = Position.PROFESSIONAL;  break;
            case "博": position = Position.DOCTOR;  break;
            default:   position = Position.OTHER;  break;
        }

        UserPo userPo = Optional.ofNullable(userRepository.findByUserid(userid))
                .orElse(
                        new UserPo(
                                response.getUserid(),
                                response.getUnionid(),
                                response.getName(),
                                response.getAvatar(),
                                authority,
                                position
                        )
                );
        userPo.setAvatar(response.getAvatar());
        userPo.setName(response.getName());
        userPo.setUnionid(response.getUnionid());
        if(userPo.getPosition() == Position.OTHER) {
            switch (Optional.ofNullable(userPo.getStuNum()).orElse("--").substring(0, 2)) {
                case "MF":
                case "mf":
                    userPo.setPosition(Position.PROFESSIONAL);
                    break;
                case "mg":
                case "MG":
                    userPo.setPosition(Position.ACADEMIC);
                    break;
                case "DG":
                case "dg":
                    userPo.setPosition(Position.DOCTOR);
                    break;
                default:
                    break;
            }
        }
        return userRepository.save(userPo);
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
    public Page<UserPo> multiQueryUser(int page, int size, String name, String position) {
        Specification<UserPo> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            predicates.add(criteriaBuilder.notEqual(root.get("authority"), UserPo.ADMIN_AUTHORITY));
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
     * @param subsidyLevelPoList
     */
    @CacheEvict(value = "subsidy", allEntries = true)
    public void setSubsidy(List<SubsidyLevelPo> subsidyLevelPoList) {
        for (SubsidyLevelPo sl : subsidyLevelPoList) {
            subsidyLevelRepository.updateSubsidy(sl.getPosition(), sl.getSubsidy());
        }
    }


    /**
     * 获取所有的绩效标准
     * @return List<SubsidyLevel>
     */
    public List<SubsidyLevelPo> listSubsidy() {
        return subsidyLevelRepository.findAll();
    }


    /**
     * 查询论文AC标准
     * @return
     */
    public List<PaperLevelPo> listPaperLevel() {
        return paperLevelRepository.findAll();
    }

    /**
     * 查询专利AC标准
     * @return
     */
    public List<PatentLevelPo> listPatentLevel() {
        return patentLevelRepository.findAll();
    }


    /**
     * 更新论文AC标准
     * @param paperLevelPos
     */
    public void updatePaperLevel(List <PaperLevelPo> paperLevelPos) {
        for (PaperLevelPo pl : paperLevelPos) {
            paperLevelRepository.updatePaperLevel(pl.getPaperType(), pl.getTotal());
        }
    }

    /**
     * 更新专利AC标准
     * @param patentLevelPos
     */
    public void updatePatentLevel(List <PatentLevelPo> patentLevelPos) {
        for (PatentLevelPo pl : patentLevelPos) {
            patentLevelRepository.updatePatentLevel(pl.getTitle(), pl.getTotal());
        }
    }


    /**
     * 更新用户信息
     * @param u
     */
    public void updateUserInfo(UserPo u) {
        log.debug(u.toString());
        userRepository.updateUserInfo(u.getId(), u.getStuNum(), u.getPosition());
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
    public List<UserPo> queryDisableUser() {
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
        var poorGuys = weeklyReportService.queryUnSubmittedWeeklyReportUser(start, end);
        if(poorGuys.size() == 0) {
            log.info(start.toString() + "没有未提交周报的poor guy");
            return;
        }
        log.info(start.toString() + "未提交周报扣分" + Arrays.toString(poorGuys.stream().map(UserPo::getName).toArray()));
        acRecordRepository.saveAll(
                poorGuys.stream()
                        .map(user -> AcRecordPo.builder()
                                .user(user)
                                .ac(AcAlgorithm.getPointOfUnsubmittedWeekReport(user))
                                .classify(AcRecordPo.NORMAL)
                                .reason(String.format(
                                        "%s 未按时提交周报",
                                        start.toLocalDate().toString()
                                ))
                                .createTime(end)
                                .build())
                        .collect(Collectors.toList())
        );
    }

    /**
     * 手动指定某一天，向当天未提交周报的博士硕士发送提醒消息
     * @param localDate
     */
    public void manualReminderToSubmitWeeklyReport(LocalDate localDate) {
        var start = localDate.atTime(0, 0, 0);
        var end = start.plusDays(1);
        messageApi.sendLinkMessage(
                "周报、绩效填写提醒",
                LocalUrlConstant.FRONTEND_PERFORMANCE_URL,
                "您还未提交本周周报，请在周日24点前提交周报并随后申请绩效",
                weeklyReportService.queryUnSubmittedWeeklyReportUser(start, end).stream()
                        .map(UserPo::getUserid)
                        .collect(Collectors.toList())
        );
    }

    public void calculateScheduleAC() {
        List<DingTalkSchedulePo> dingTalkSchedulePoList =dingTalkScheduleRepository.getDingTalkSchedulesByAcCalculatedFalse();
        for(DingTalkSchedulePo dingTalkSchedulePo : dingTalkSchedulePoList){
            LocalDateTime now=LocalDateTime.now();
            if(now.compareTo(dingTalkSchedulePo.getEnd())>=0){
                //获取改日程请假列表，并获取oa通过的同学的列表
                List<String> osNotPassUserIdList=new LinkedList<>();
                dingTalkSchedulePo.getAbsentOAList().forEach(absentOA -> {
                    absentOA.setPass(oaApi.getOAOutCome(absentOA.getProcessInstanceId())==1);
                    if(!absentOA.isPass()) osNotPassUserIdList.add(absentOA.getUser().getUserid());
                });
                //获取需要扣分的userId
                List<String> absentUserIdList=scheduleApi.getAbsentList(dingTalkSchedulePo);
                absentUserIdList.removeAll(osNotPassUserIdList);
                //进行扣分
                if(absentUserIdList.size()!=0){
                    List<DingTalkScheduleDetailPo> detailList= dingTalkSchedulePo.getDingTalkScheduleDetailList();
                    detailList.forEach(detail -> {
                        boolean isContain = absentUserIdList .stream().anyMatch(x-> x.equals(detail.getUser().getUnionid()));
                        if(isContain) {
                            AcRecordPo acRecordPO =new AcRecordPo(detail.getUser(),
                                    null,
                                    -1*absentACPunishment,
                                    detail.getDingTalkSchedule().getStart().toString()+"会议缺席",
                                    6,LocalDateTime.now());
                            acRecordRepository.save(acRecordPO);
                            detail.setAcRecord(acRecordPO);
                        }
                    });
                }
                dingTalkSchedulePo.setAcCalculated(true);
                dingTalkScheduleRepository.save(dingTalkSchedulePo);
            }
        }
    }

}
