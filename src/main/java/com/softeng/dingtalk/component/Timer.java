package com.softeng.dingtalk.component;

import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalTime;
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

    public void checkVote() {
        List<Vote> votes = voteRepository.listByStatus(); //拿到没有结束的投票

        LocalTime nowtime = LocalTime.now();
        log.debug(nowtime.toString());
        for (Vote v : votes) {
            if (v.getEndTime().isBefore(nowtime)) {
                voteService.updateVote(v.getId());
                // todo 钉钉发送消息
                //
            }
        }

    }


}
