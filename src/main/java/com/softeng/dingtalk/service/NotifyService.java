package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.CheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author zhanyeye
 * @description 消息通知
 * @create 3/2/2020 4:16 PM
 */
@Service
@Transactional
@Slf4j
public class NotifyService {
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    VoteRepository voteRepository;

    public void reviewDcMessage(DcRecord dc) {

        String title = new StringBuilder().append("第").append(dc.getWeek()).append("周绩效").toString();
        String content = new StringBuilder().append("DC: ").append(dc.getDc()).append(",  AC: ").append(dc.getAc()).toString();
        Message message = new Message(title, content, dc.getApplicant().getId());

        messageRepository.save(message);
    }

    public void updateDcMessage(DcRecord dc) {
        String title = new StringBuilder().append("第").append(dc.getWeek()).append("周绩效 被更新").toString();
        String content = new StringBuilder().append("DC: ").append(dc.getDc()).append(",  AC: ").append(dc.getAc()).toString();
        Message message = new Message(title, content, dc.getApplicant().getId());

        messageRepository.save(message);
    }

    public Slice<Message> listUserMessage(int uid, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createTime"));
        return messageRepository.findByUid(uid, pageable);

    }

    public void paperAcMessage(int pid, boolean result) {
        Paper paper = paperRepository.findById(pid).get();
        for (PaperDetail pd : paper.getPaperDetails()) {
            double acsum = acRecordRepository.getUserAcSum(pd.getUser().getId());
            String title = "论文: " + paper.getTitle() + (result ? "投稿成功":"投稿失败");
            String content = "AC: " + pd.getAc() + "  当前总AC: " + acsum;
            Message msg = new Message(title, content, pd.getUser().getId());
            messageRepository.save(msg);
        }
    }

    public void voteAcMessage(int pid, boolean result) {
        Vote v = paperRepository.findVoteById(pid);
        String papertitel = paperRepository.getPaperTitleById(pid);
        String title;
        String content;
        for (VoteDetail vd : v.getVoteDetails()) {
            double acsum = acRecordRepository.getUserAcSum(vd.getUser().getId());
            if (vd.getResult() == result) {
                title = "投票预测正确, " + papertitel + "投稿成功";
                content = "AC: + 1    " + "当前总AC: " + acsum;
            } else {
                title = "投票预测错误" + papertitel + "投稿失败";
                content = "AC: - 1    " + "当前总AC: " + acsum;
            }
            Message msg = new Message(title, content, vd.getUser().getId());
            messageRepository.save(msg);
        }

    }

}
