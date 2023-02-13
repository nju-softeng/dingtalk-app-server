package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.po.AcItemPo;
import com.softeng.dingtalk.po.DcRecordPo;
import com.softeng.dingtalk.vo.ApplyVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 8:21 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ApplicationServiceTests {
    @Autowired
    ApplicationService applicationService;
    @Autowired
    DateUtils dateUtils;

    @Test
    public void testAddApplication() {
        ApplyVO vo = new ApplyVO();
        vo.setAuditorid(4);
        vo.setDate(LocalDate.now());
        vo.setDvalue(0.5);
        List<AcItemPo> acItemPos = new ArrayList<>();
        acItemPos.add(new AcItemPo("申请ac1", 1));
        acItemPos.add(new AcItemPo("申请ac2", 1));
        vo.setAcItemPos(acItemPos);
        applicationService.addApplication(vo, 1);
    }


    @Test
    public void testUpdateApplication() {
        ApplyVO vo = new ApplyVO();
        vo.setId(1296);
        vo.setAuditorid(4);
        vo.setDate(LocalDate.now());
        vo.setDvalue(0.1);
        // 如果不 new 一个数组会出现空指针异常
        vo.setAcItemPos(new ArrayList<>());
        applicationService.updateApplication(vo, 1);
    }


    @Test
    public void testAddApplicationByAuditor() {
        ApplyVO vo = new ApplyVO();
        vo.setAuditorid(6);
        vo.setDate(LocalDate.now());
        vo.setDvalue(0.5);
        vo.setCvalue(0.8);
        List<AcItemPo> acItemPos = new ArrayList<>();
        acItemPos.add(new AcItemPo("申请ac1", 1));
        acItemPos.add(new AcItemPo("申请ac2", 2));
        vo.setAcItemPos(acItemPos);
        applicationService.addApplicationByAuditor(vo, 3);
    }


    @Test
    public void testUpdateApplicationByAuditor() {
        ApplyVO vo = new ApplyVO();
        vo.setId(1301);
        vo.setAuditorid(4);
        vo.setDate(LocalDate.now());
        vo.setDvalue(0.5);
        vo.setCvalue(0.8);
        List<AcItemPo> acItemPos = new ArrayList<>();
        acItemPos.add(new AcItemPo("申请ac1", 1));
        acItemPos.add(new AcItemPo("申请ac2", 2));
        vo.setAcItemPos(acItemPos);
        applicationService.updateApplication(vo, 4);
    }


    @Test
    public void testSetByAuditor() {
        ApplyVO vo = new ApplyVO();
        vo.setAuditorid(4);
        vo.setDate(LocalDate.now());
        vo.setDvalue(0.5);
        vo.setCvalue(0.8);
        List<AcItemPo> acItemPos = new ArrayList<>();
        acItemPos.add(new AcItemPo("申请ac1", 1));
        acItemPos.add(new AcItemPo("申请ac2", 2));
        vo.setAcItemPos(acItemPos);
        DcRecordPo dcRecordPO = new DcRecordPo().setByAuditor(5, vo, dateUtils.getDateCode(vo.getDate()));
        log.info(dcRecordPO.toString());
    }

    @Test
    public void testGetRecentAuditor() {
        log.info("{}",applicationService.getRecentAuditor(4));
    }

}
