package com.softeng.dingtalk.performance;

import com.softeng.dingtalk.aspect.ACBlockchainAspect;
import com.softeng.dingtalk.controller.*;
import com.softeng.dingtalk.vo.AbsentOAVO;
import com.softeng.dingtalk.vo.DingTalkScheduleVO;
import lombok.extern.slf4j.Slf4j;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Transactional
public class PerformanceTest {
    @Autowired
    PatentController patentController;
    @Autowired
    PaperController paperController;
    @Autowired
    ReimburseController reimburseController;
    @Autowired
    ProjectPropertyController projectPropertyController;
    @Autowired
    ProcessPropertyController processPropertyController;
    @Autowired
    EventPropertyController eventPropertyController;
    @Autowired
    DissertationPropertyController dissertationPropertyController;
    @Autowired
    DingTalkScheduleController dingTalkScheduleController;
    @Autowired
    ACBlockchainAspect acBlockchainAspect;
    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();
    int i=1;
    public static List timeCostList=new ArrayList<>();
    int threadNum=20;
    public static DingTalkScheduleVO dingTalkScheduleVO;
    public static AbsentOAVO absentOAVO;
    @BeforeClass
    public static void beforeTest(){
        dingTalkScheduleVO=new DingTalkScheduleVO(140,"test", LocalDateTime.now(),LocalDateTime.now().plusDays(1L),true,null);
        List<Integer> list=new LinkedList<>();
        list.add(104);
        list.add(105);
        dingTalkScheduleVO.setAttendeesIdList(list);
        absentOAVO=new AbsentOAVO("事假","test");

    }

    @Test
    @PerfTest(invocations = 20,threads = 20)
    public void test(){
        Long start=System.currentTimeMillis();
        dingTalkScheduleController.addAbsentOA(3,105,absentOAVO);
        timeCostList.add(System.currentTimeMillis()-start);
    }

    @AfterClass
    public static void singleTest(){
        log.info("平均延时");
        log.info(String.valueOf(computeAverageLatency(timeCostList)));
    }


    public static Long computeAverageLatency(List list){
        Long total=0L;
        for(int i=0;i<list.size();i++){
            total+=(Long)list.get(i);
        }
        return total/list.size();
    }
}
