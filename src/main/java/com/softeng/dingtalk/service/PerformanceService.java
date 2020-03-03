package com.softeng.dingtalk.service;

import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import com.softeng.dingtalk.repository.TopupRepository;
import com.softeng.dingtalk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 9:48 AM
 */
@Service
@Transactional
@Slf4j
public class PerformanceService {
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    TopupRepository topupRepository;
    @Autowired
    UserRepository userRepository;




    // Gain = Base * DC * (1+ (AC/50)) + Topup
    public void computeSalary(int uid, int yearmonth) {
        double dc = dcSummaryRepository.getDcTotal(uid, yearmonth);
        double ac = acRecordRepository.getUserAcByDate(uid, yearmonth); // 获取到目前为止用户的AC总和
        double topup = topupRepository.getByUserid(uid); // 获取用户的 Topup
        double base;
        if (userRepository.getUserDegree(uid) == 0) {
            base = 150;
        } else {
            base = 250;
        }
        double total = Math.round(base * dc * (1 + (ac/50)) + topup);
        dcSummaryRepository.updateSalary(uid, ac, topup, total);
        log.debug(total + "");
    }

    public List<Map<String, Object>> listUserAc(int uid) {
        return acRecordRepository.listUserAc(uid);
    }

    public List<Map<String, Object>> listDcSummaryVO(LocalDate date) {
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        log.debug(yearmonth + "");
        return dcSummaryRepository.listDcSummary(yearmonth);
    }


    public List<Map<String, Object>> listAcSummary() {
        return acRecordRepository.listAcSummary();
    }

}
