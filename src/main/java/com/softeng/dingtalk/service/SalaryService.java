package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description
 * @create 2/27/2020 4:57 PM
 */
@Service
@Transactional
@Slf4j
public class SalaryService {
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    TopupRepository topupRepository;
    @Autowired
    UserRepository userRepository;




    // Gain = Base * DC * (1+ (AC/50)) + Topup
//    public void updateUser(int uid) {
//        LocalDate date = LocalDate.now();
//        int yearmonth = date.getYear() * 100  + date.getMonthValue();
//        double dc = dcSummaryRepository.getDcTotal(uid, yearmonth);
//        double ac = acRecordRepository.getUserAcByDate(uid, LocalDate.now()); // 获取到目前为止用户的AC总和
//        double topup = topupRepository.getByUserid(uid); // 获取用户的 Topup
//        double base;
//        if (userRepository.getUserDegree(uid) == 0) {
//            base = 150;
//        } else {
//            base = 250;
//        }
//        double total = Math.round(base * dc * (1 + (ac/50)) + topup);
//        log.debug(total + "");
//    }

}
