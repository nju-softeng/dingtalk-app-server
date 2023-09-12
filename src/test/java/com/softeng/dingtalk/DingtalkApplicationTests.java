package com.softeng.dingtalk;

import com.softeng.dingtalk.component.encryptor.EncryptorComponent;
import com.softeng.dingtalk.entity.InternalPaper;
import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.enums.PaperType;
import com.softeng.dingtalk.dao.repository.InternalPaperRepository;
import com.softeng.dingtalk.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DingtalkApplicationTests {

    @Autowired
    VoteService voteService;
    @Autowired
    InternalPaperRepository internalPaperRepository;
    @Autowired
    EncryptorComponent encryptorComponent;

    @Test
    public void test() {
        log.info(encryptorComponent.encrypt(Map.of("uid", 2, "authorityid", 0)));
    }

    @Test
    public void test2() {
        Paper paper = new InternalPaper("标题11111", "journal", PaperType.JOURNAL_A, LocalDate.now(), true, null,null,null,null);
        log.debug(paper.getTitle());
    }
}
