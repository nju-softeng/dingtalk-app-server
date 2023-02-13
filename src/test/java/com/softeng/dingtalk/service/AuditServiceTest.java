package com.softeng.dingtalk.service;


import com.softeng.dingtalk.po.AcItemPo;
import com.softeng.dingtalk.vo.CheckVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 12/26/2019 3:46 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AuditServiceTest {
    @Autowired
    AuditService auditService;

    @Test
    public void testUpdateAuditResult() {
        List<AcItemPo> acItemPos = new ArrayList<>();
        acItemPos.add(new AcItemPo("申请ac1", 1));
        acItemPos.add(new AcItemPo("申请ac2", 1));
        CheckVO vo = new CheckVO(1329, 0.5, 0.777, 2, acItemPos);
        auditService.updateAuditResult(vo);
    }

    @Test
    public void testUpdateDcSummary() {
        auditService.updateDcSummary(1, 202104,3);
    }




}
