package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.PracticeConvertor;
import com.softeng.dingtalk.dao.repository.InternshipPeriodRecommendedRepository;
import com.softeng.dingtalk.dto.req.PracticeReq;
import com.softeng.dingtalk.dto.resp.PracticeResp;
import com.softeng.dingtalk.enums.PracticeStateEnum;
import com.softeng.dingtalk.exception.CustomExceptionEnum;
import com.softeng.dingtalk.entity.InternshipPeriodRecommended;
import com.softeng.dingtalk.entity.Practice;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.dao.repository.PracticeRepository;
import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.utils.StreamUtils;
import com.softeng.dingtalk.vo.PracticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
@Slf4j
public class PracticeService {
    public final static String adminUserName="Jason";
    @Autowired
    PracticeRepository practiceRepository;
    @Autowired
    UserRepository userRepository;
    @Resource
    private PracticeConvertor practiceConvertor;
    @Resource
    private InternshipPeriodRecommendedRepository internshipPeriodRecommendedRepository;

    @Deprecated
    public void addPractice(PracticeVO practiceVO,int uid){
        Practice practice =new Practice(userRepository.findById(uid).get(),practiceVO.getCompanyName(),practiceVO.getDepartment(),practiceVO.getStart(),practiceVO.getEnd(),practiceVO.getState());
        practiceRepository.save(practice);
    }

    public void deletePractice(int id,int uid){
        Practice practice =practiceRepository.findById(id).get();
        User applicant =practice.getUser();
//        if(practice.getUser().getId()!=uid){
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无删除权限！");
//        }
        CustomExceptionEnum.INTERNSHIP_APPLICATION_DELETION_DENIED.throwIf(practice.getUser().getId()!=uid);

//        如果删除的实习申请为审核通过状态，且当前正在实习期内，删除该申请前需要将用户的工作状态置为”在校“
        if(practice.getState() == PracticeStateEnum.ACCEPTED.getValue()) {
            LocalDate cur = LocalDate.now();
            LocalDate start = practice.getStart();
            LocalDate end = practice.getEnd();
            if((cur.isAfter(start) || cur.isEqual(start)) && (cur.isBefore(end) || cur.isEqual(end))) {
                applicant.setWorkState(false);
                userRepository.save(applicant);
            }
        }

        practiceRepository.delete(practice);
    }

    @Deprecated
    public void modifyPractice(PracticeVO practiceVO){
        if(practiceRepository.findById(practiceVO.getId()).get().getState()==1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"审核已通过，无需修改！");
        }
        Practice practice =practiceRepository.findById(practiceVO.getId()).get();
        practice.update(practiceVO.getCompanyName(),practiceVO.getDepartment(),practiceVO.getStart(),practiceVO.getEnd(),practiceVO.getState());
        practiceRepository.save(practice);
    }

//    public void audit(int id, int uid, boolean isPass){
//        Practice practice=practiceRepository.findById(id).get();
//        if(isPass){
//            practice.setState(1);
//        } else {
//            practice.setState(-1);
//        }
//        practiceRepository.save(practice);
//    }

    public List<Practice> getPracticeList(int uid){
        User user =userRepository.findById(uid).get();
        if(user.getName().equals(adminUserName)){
            return practiceRepository.findAllByStateEquals(0);
        } else {
            return practiceRepository.findAllByUserEquals(user);
        }
    }

    public Map<String, Object> queryPracticeList(int page, int size, PracticeReq practiceReq) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Specification<Practice> practiceSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(practiceReq.getUserId() != 0)
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), practiceReq.getUserId()));
            if(practiceReq.getState() != -2)
                predicates.add(criteriaBuilder.equal(root.get("state"), practiceReq.getState()));
            Predicate[] arr = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arr));
        });
        Page<Practice> practicePage = practiceRepository.findAll(practiceSpecification, pageable);
        List<PracticeResp> practiceRespList = StreamUtils.map(practicePage.toList(), (practice -> practiceConvertor.entity2Resp(practice)));
        return Map.of("list", practiceRespList, "total", practicePage.getTotalElements());
    }

    private boolean checkDateConflict(int uid, LocalDate start, LocalDate end) {
        Specification<Practice> practiceSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), uid));
            Predicate[] arr = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arr));
        });
        AtomicBoolean res = new AtomicBoolean(false);
        practiceRepository.findAll(practiceSpecification).forEach(practice -> {
            if(practice.getState() == PracticeStateEnum.REJECTED.getValue()) return;
            if((start.isAfter(practice.getStart()) || start.isEqual(practice.getStart())) && (start.isBefore(practice.getEnd()) || start.isEqual(practice.getEnd()))
                || (end.isAfter(practice.getStart()) || end.isEqual(practice.getStart())) && (end.isBefore(practice.getEnd()) || end.isEqual(practice.getEnd()))) {
                res.set(true);
            }
        });
        return res.get();
    }

    public void addPractice(PracticeReq practiceReq){
        LocalDate start = practiceReq.getStart();
        LocalDate end = practiceReq.getEnd();

        CustomExceptionEnum.START_DATE_IS_AFTER_END_DATE.throwIf(start.isAfter(end));

        CustomExceptionEnum.INTERNSHIP_APPLICATION_CONFLICT
                .throwIf(checkDateConflict(practiceReq.getUserId(), start, end));

//        如果申请中的时间段在推荐实习周期之内，直接通过该申请
        InternshipPeriodRecommended periodRecommended = internshipPeriodRecommendedRepository.findTop();
        LocalDate startRecommended = periodRecommended.getStart();
        LocalDate endRecommended = periodRecommended.getEnd();
        if((startRecommended.isBefore(start) || startRecommended.isEqual(start)) && (endRecommended.isAfter(end)) || endRecommended.isEqual(end)){
            practiceReq.setState(PracticeStateEnum.ACCEPTED.getValue());
            LocalDate cur = LocalDate.now();
            User applicant = userRepository.findById(practiceReq.getUserId()).get();
            if((cur.isAfter(start) || cur.isEqual(start)) && (cur.isBefore(end) || cur.isEqual(end))) {
                applicant.setWorkState(false);
                userRepository.save(applicant);
            }
        }



        Practice practice = practiceConvertor.req2Entity(practiceReq);
        practiceRepository.save(practice);
    }

    public void modifyPractice(PracticeReq practiceReq){
        CustomExceptionEnum.INTERNSHIP_APPLICATION_AUDITED_ALREADY
                .throwIf(practiceRepository.findById(practiceReq.getId()).get().getState() != PracticeStateEnum.AUDITING.getValue());

        if(practiceReq.getState() == PracticeStateEnum.ACCEPTED.getValue()) {
            LocalDate cur = LocalDate.now();
            LocalDate start = practiceReq.getStart();
            LocalDate end = practiceReq.getEnd();
            if((cur.isAfter(start) || cur.isEqual(start)) && (cur.isBefore(end)) || cur.isEqual(start)) {
                User user = userRepository.findById(practiceReq.getUserId()).get();
                user.setWorkState(true);
                userRepository.save(user);
            }
        }

        Practice practice = practiceConvertor.req2Entity(practiceReq);
        practiceRepository.save(practice);
    }
}
