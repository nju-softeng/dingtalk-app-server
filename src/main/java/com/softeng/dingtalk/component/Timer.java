package com.softeng.dingtalk.component;

import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.repository.PaperRepository;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
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


    @Scheduled(cron = "0 * * * * ?")
    public void checkVote() {
        //拿到没有结束的投票
        List<Vote> votes = voteService.listUnderwayVote();
        if (votes.size() != 0) {
            LocalDateTime now = LocalDateTime.now();
            log.debug("定时器执行：" + now.toString());
            for (Vote v : votes) {
                if (v.getDeadline().isBefore(now)) {
                    //更新
                    v = voteService.updateVote(v);
                    // todo 钉钉发送消息
                    log.debug("钉钉发送消息");
                    Map map = paperRepository.getPaperInfo(v.getId());
                    if (map.size() != 0) {
                        int pid = (int)map.get("id");
                        String title = map.get("title").toString();
                        dingTalkUtils.sendVoteResult(pid, title, v.getResult(), v.getAccept(), v.getTotal());
                    }
                }
            }
        }


    }


}
