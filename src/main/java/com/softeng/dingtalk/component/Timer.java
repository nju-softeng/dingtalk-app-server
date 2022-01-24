package com.softeng.dingtalk.component;

import com.softeng.dingtalk.api.MessageApi;
import com.softeng.dingtalk.constant.LocalUrlConstant;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.ExternalPaperRepository;
import com.softeng.dingtalk.repository.InternalPaperRepository;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.service.InitService;
import com.softeng.dingtalk.service.VoteService;
import com.softeng.dingtalk.service.WeeklyReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    WeeklyReportService weeklyReportService;
    @Autowired
    AcRecordRepository acRecordRepository;

    @Autowired
    MessageApi messageApi;

    /**
     * 每周周日23点扫描一次，给当天还未提交周报的博士、硕士发送提醒消息
     */
    @Scheduled(cron = "0 0 23 ? * SUN")
    public void weeklyReportAndPerformanceFillingReminder() {
        log.info(LocalDate.now() + "定时扫描提醒周报和绩效填写");
        var end = LocalDate.now().atTime(0, 0, 0).plusDays(1);
        var start = end.minusDays(1);
        messageApi.sendLinkMessage(
                "周报、绩效填写提醒",
                LocalUrlConstant.FRONTEND_PERFORMANCE_URL,
                "您还未提交本周周报，请在周日24点前提交周报并随后申请绩效",
                weeklyReportService.queryUnSubmittedWeeklyReportUser(start, end).stream()
                        .map(User::getUserid)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 每周一凌晨2点扫描一次，查询周日一整天没有提交周报的博士、硕士，每人扣 1 ac
     */
    @Scheduled(cron = "0 0 2 ? * MON")
    public void deductedPointsUnsubmittedWeeklyReport() {
        log.info(LocalDate.now() + " 定时扫描扣分");
        var end = LocalDate.now().atTime(0, 0, 0);
        var start = end.minusDays(1);
        var users = weeklyReportService.queryUnSubmittedWeeklyReportUser(start, end);
        acRecordRepository.saveAll(
                users.stream()
                        .map(user -> AcRecord.builder()
                                .user(user)
                                .ac(AcAlgorithm.getPointOfUnsubmittedWeekReport(user))
                                .classify(AcRecord.NORMAL)
                                .reason(String.format(
                                        "%s 未按时提交周报",
                                        end.minusDays(1).toLocalDate().toString()
                                ))
                                .createTime(end)
                                .build())
                        .collect(Collectors.toList())
        );
    }

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
            Paper paper = v.isExternal() ?
                    externalPaperRepository.findByVid(v.getId()) :
                    internalPaperRepository.findByVid(v.getId());
            messageApi.sendActionCard(
                    "投票结果",
                    voteResultInfo(paper.getTitle(), v.getResult(), v.getAccept(), v.getTotal()),
                    "查看详情",
                    generateVoteDetailUrl(paper.isExternal(), paper.getId())
            );
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
