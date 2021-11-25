package com.softeng.dingtalk.component;

import com.softeng.dingtalk.api.MessageApi;
import com.softeng.dingtalk.entity.ExternalPaper;
import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.repository.ExternalPaperRepository;
import com.softeng.dingtalk.repository.InternalPaperRepository;
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
    InternalPaperRepository internalPaperRepository;
    @Autowired
    InitService initService;
    @Autowired
    ExternalPaperRepository externalPaperRepository;

    @Autowired
    MessageApi messageApi;

    /**
     * 每20s扫描一次，看是否有待启动的投票
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void checkVote() {
        LocalDateTime now = LocalDateTime.now();

        // (针对外部评审投票) 检查是否有投票需要开始
        List<Vote> upcomingVotes = voteService.listUpcomingVote(now);
        upcomingVotes.forEach(v -> {
            // 标注该投票已经开始
            v.setStarted(true);
            voteRepository.save(v);

            ExternalPaper externalPaper = externalPaperRepository.findByVid(v.getId());
            if (externalPaper != null) {
                // 发送投票开始的消息
                String markdown = startVoteInfo(externalPaper.getTitle(), v.getEndTime());
                String url = generateVoteDetailUrl(true, externalPaper.getId());
                messageApi.sendActionCard("外部评审投票", markdown, "前往投票", url);
            }
        });
    }

    /**
     * 每20s扫描一次，看是否有待结束的投票
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void checkVote2() {
        // 检查是否有需要被结束的投票
        LocalDateTime now = LocalDateTime.now();

        List<Vote> votes = voteService.listUnderwayVote(now);
        votes.forEach(v -> {
            // 更新汇总投票结果
            v = voteService.updateVote(v);
            Paper paper = v.isExternal() ? externalPaperRepository.findByVid(v.getId()) : internalPaperRepository.findByVid(v.getId());
            String url = generateVoteDetailUrl(paper.isExternal(), paper.getId());
            String markdown = voteResultInfo(paper.getTitle(), v.getResult(), v.getAccept(), v.getTotal());
            messageApi.sendActionCard("投票结果", markdown, "查看详情", url);
        });
    }

    /**
     * 每月1日3点执行 initDcDummary 方法
     */
    @Scheduled(cron = "0 0 3 1 * ?")
    public void initMonthlyDcSummary() {
        initService.initDcSummary();
    }

    /**
     * 投票消息卡片跳转的 url
     * @param isExternal 是否是外部论文
     * @param pid 论文id
     * @return
     */
    private String generateVoteDetailUrl(boolean isExternal, int pid) {
        return (isExternal ? "/paper/ex-detail/" : "/paper/in-detail/") + pid + "/vote";
    }

    /**
     * 确认投票结果的消息模板
     * @param title 论文标题
     * @param result 投票预测结果
     * @param acceptCnt accept数量
     * @param totalCnt 总投票数量
     * @return
     */
    private String voteResultInfo(String title, boolean result, int acceptCnt, int totalCnt) {
        return new StringBuilder().append(" #### 投票结果 \n ##### 论文： ").append(title)
                .append(" \n 最终结果： ").append(result ? "Accept" : "reject")
                .append("  \n  Accept: ").append(acceptCnt).append(" 票  \n ")
                .append("Reject: ").append(totalCnt - acceptCnt).append(" 票  \n ")
                .append("已参与人数： ").append(totalCnt).append("人  \n ").toString();
    }

    /**
     * 发起投票的消息模板
     * @param title 论文标题
     * @param dateTime 截至时间
     * @return
     */
    private String startVoteInfo(String title, LocalDateTime dateTime) {
        return " #### 投票 \n ##### 论文： " + title + " \n 截止时间: " + dateTime.toLocalTime().toString();
    }

}
