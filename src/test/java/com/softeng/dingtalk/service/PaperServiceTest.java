package com.softeng.dingtalk.service;

import com.softeng.dingtalk.enums.PaperType;
import com.softeng.dingtalk.vo.AuthorVO;
import com.softeng.dingtalk.vo.ExternalPaperVO;
import com.softeng.dingtalk.vo.InternalPaperVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhanyeye
 * @Description
 * @Date 01/11/2021
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class PaperServiceTest {
    @Autowired
    PaperService paperService;

    public List<AuthorVO> generateAuthorVOs() {
        List<AuthorVO> authorVOS = new ArrayList<>();
        authorVOS.add(new AuthorVO(1, "name1", 1));
        authorVOS.add(new AuthorVO(2, "name2", 2));
        authorVOS.add(new AuthorVO(3, "name3", 3));
        return authorVOS;
    }


    @Test
    public void testAddInternalPaper() {
        InternalPaperVO vo = new InternalPaperVO(null, "internal_paper1", "test", PaperType.JOURNAL_A, null, generateAuthorVOs(), true, null, 1, null, null, -1,null,null,null,null);
        paperService.addInternalPaper(vo);
    }

    @Test
    public void testAddExternalPaper() {
        ExternalPaperVO vo = new ExternalPaperVO(null, "external_paper1", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        paperService.addExternalPaper(vo);
    }

    @Test
    public void testUpdateInternalPaper() {
        InternalPaperVO vo = new InternalPaperVO(51, "internal_xxxx", "xxxxx", PaperType.JOURNAL_B, null, generateAuthorVOs(), true, null, 1, null, null, -1,null ,null,null,null);
        paperService.updateInternalPaper(vo);
    }

    @Test
    public void testUpdateExternalPaperShouldOk() {
        ExternalPaperVO vo = new ExternalPaperVO(12, "external_xxxx", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));
        paperService.updateExternalPaper(vo);
    }

    @Test
    public void testListExternalPaper() {

    }
}
