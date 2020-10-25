package com.softeng.dingtalk.component;

import com.softeng.dingtalk.entity.ExternalPaper;
import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.repository.ExternalPaperRepository;
import com.softeng.dingtalk.repository.PaperRepository;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.service.InitService;
import com.softeng.dingtalk.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description 定时器
 * @create 2/9/2020 12:30 PM
 */

@Slf4j
@Component
@Transactional
public class Timer {

    @Autowired
    VoteRepository voteRepository;
    @Autowired
    VoteService voteService;
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    InitService initService;
    @Autowired
    ExternalPaperRepository externalPaperRepository;

    /**
     * 每分钟扫描一次，看是否有待启动的投票，或者待结束的投票
     */
    @Scheduled(cron = "0 * * * * ?")
    public void checkVote() {
        LocalDateTime now = LocalDateTime.now();

        // (针对外部评审投票) 检查是否有投票需要开始
        List<Vote> upcomingVotes = voteService.listUpcomingVote(now);
        if (upcomingVotes.size() != 0) {
            for (Vote v : upcomingVotes) {
                // 标注该投票已经开始
                v.setStarted(true);
                voteRepository.save(v);

                ExternalPaper externalPaper = externalPaperRepository.findByVid(v.getId());
                if (externalPaper != null) {
                    // 发送投票开始的消息
                    dingTalkUtils.sendVoteMsg(v.getPid(), true, externalPaper.getTitle(), v.getEndTime().toString(), null);
                }
            }
        }

        // 检查是否有需要被结束的投票
        List<Vote> votes = voteService.listUnderwayVote();
        if (votes.size() != 0) {
            for (Vote v : votes) {
                if (v.getEndTime().isBefore(now)) {
                    // 更新汇总投票结果
                    v = voteService.updateVote(v);
                    // 发送投票消息
                    if (v.isExternal()) {
                        // 如果是外部投票
                        ExternalPaper externalPaper = externalPaperRepository.findByVid(v.getId());
                        if (externalPaper != null) {
                            dingTalkUtils.sendVoteResult(externalPaper.getId(), externalPaper.getTitle(), v.getResult(), v.getAccept(), v.getTotal(), v.isExternal());
                        }
                    } else {
                        // 如果是内部投票
                        Paper internalpaper = paperRepository.findByVid(v.getId());
                        if (internalpaper != null) {
                            dingTalkUtils.sendVoteResult(internalpaper.getId(), internalpaper.getTitle(), v.getResult(), v.getAccept(), v.getTotal(), v.isExternal());
                        }
                    }
                }
            }
        }
    }


    /**
     * 每月1日3点执行 initDcDummary 方法
     */
    @Scheduled(cron = "0 0 3 1 * ?")
    public void initMonthlyDcSummary() {
        initService.initDcSummary();
    }


}
