package com.softeng.pms.component;

import com.softeng.pms.entity.ExternalPaper;
import com.softeng.pms.entity.Paper;
import com.softeng.pms.entity.Vote;
import com.softeng.pms.repository.ExternalPaperRepository;
import com.softeng.pms.repository.PaperRepository;
import com.softeng.pms.repository.VoteRepository;
import com.softeng.pms.service.InitService;
import com.softeng.pms.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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
    @Scheduled(cron = "0/20 * * * * ?")
    public void checkVote() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        log.debug("checkout1: " + now.toString());
        log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

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
                    String markdown = new StringBuilder(" #### 投票 \n ##### 论文： ").append(externalPaper.getTitle())
                            .append(" \n 截止时间: ").append(v.getEndTime().toLocalTime().toString()).toString();
                    String url = new StringBuilder().append("/paper/ex-detail/").append(externalPaper.getId()).append("/vote").toString();
                    dingTalkUtils.sendActionCard("外部评审投票", markdown, "前往投票", url);
                }
            }
        }
    }


    @Scheduled(cron = "0/20 * * * * ?")
    public void checkVote2() {
        // 检查是否有需要被结束的投票
        LocalDateTime now = LocalDateTime.now();

        log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        log.debug("checkout2: " + now.toString());
        log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

        List<Vote> votes = voteService.listUnderwayVote(now);
        if (votes.size() != 0) {
            for (Vote v : votes) {
                // 更新汇总投票结果
                v = voteService.updateVote(v);

                String title ="", url = "";
                if (v.isExternal()) {
                    ExternalPaper externalPaper = externalPaperRepository.findByVid(v.getId());
                    title = externalPaper.getTitle();
                    url = new StringBuilder().append("/paper/ex-detail/").append(externalPaper.getId()).append("/vote").toString();
                } else {
                    Paper paper = paperRepository.findByVid(v.getId());
                    title = paper.getTitle();
                    url =new StringBuilder().append("/paper/in-detail/").append(paper.getId()).append("/vote").toString();
                }

                String markdown = new StringBuilder().append(" #### 投票结果 \n ##### 论文： ").append(title)
                        .append(" \n 最终结果： ").append(v.getResult() ? "Accept" : "reject")
                        .append("  \n  Accept: ").append(v.getAccept()).append(" 票  \n ")
                        .append("Reject: ").append(v.getTotal() - v.getAccept()).append(" 票  \n ")
                        .append("已参与人数： ").append(v.getTotal()).append("人  \n ").toString();

                dingTalkUtils.sendActionCard("投票结果", markdown, "查看详情", url);

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
