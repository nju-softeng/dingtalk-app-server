package com.softeng.pms.service;

import com.softeng.pms.entity.*;
import com.softeng.pms.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    @Autowired
    ExternalPaperRepository externalPaperRepository;


    /**
     * dc 审核消息
     * @param dc
     */
    public void reviewDcMessage(DcRecord dc) {
        String month =  String.valueOf(dc.getYearmonth() % 100);
        String title = new StringBuilder().append(month).append("月第").append(dc.getWeek()).append("周绩效").toString();
        String content = new StringBuilder().append("C值: ").append(dc.getCvalue()).append(",  DC值: ").append(dc.getDc()).append(",  AC值: ").append(dc.getAc()).toString();
        Message message = new Message(title, content, dc.getApplicant().getId());

        messageRepository.save(message);
    }


    /**
     * dc 审核结果更新消息
     * @param dc
     */
    public void updateDcMessage(DcRecord dc) {
        String month =  String.valueOf(dc.getYearmonth() % 100);
        String title = new StringBuilder().append(month).append("月第").append(dc.getWeek()).append("周绩效 被更新").toString();
        String content = new StringBuilder().append("C值: ").append(dc.getCvalue()).append(",  DC值: ").append(dc.getDc()).append(",  AC值: ").append(dc.getAc()).toString();
        Message message = new Message(title, content, dc.getApplicant().getId());

        messageRepository.save(message);
    }


    /**
     * 查询指定用户的消息
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public Page<Message> listUserMessage(int uid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return messageRepository.findByUid(uid, pageable);
    }


    /**
     * 论文AC消息
     * @param pid
     * @param result
     */
    public void paperAcMessage(int pid, boolean result) {
        Paper paper = paperRepository.findById(pid).get();
        String papertitel = paper.getTitle();
        int len = 20 < papertitel.length() ? 20 : papertitel.length();
        for (PaperDetail pd : paper.getPaperDetails()) {
           //double acsum = acRecordRepository.getUserAcSum(pd.getUser().getId());
            String title = "论文: " + papertitel.substring(0, len) + (result ? "... 投稿成功":"... 投稿失败");
            String content = "AC: " + pd.getAc();
            Message msg = new Message(title, content, pd.getUser().getId());
            messageRepository.save(msg);
        }
    }


    /**
     * 投票AC消息
     * @param vid
     * @param result
     */
    public void voteAcMessage(int vid, boolean result) {
        Vote v = voteRepository.findById(vid).get();
        String papertitel;
        if (v.isExternal()) {
            // 如果是外部评审
            ExternalPaper externalPaper = externalPaperRepository.findByVid(vid);
            papertitel = "外部评审：" + externalPaper.getTitle();
        } else {
            // 如果是内部评审
            Paper paper = paperRepository.findByVid(vid);
            papertitel = "内部评审：" + paper.getTitle();
        }
        String title;
        String content;
        int len = 24 < papertitel.length() ? 24 : papertitel.length();
        for (VoteDetail vd : v.getVoteDetails()) {
            //double acsum = acRecordRepository.getUserAcSum(vd.getUser().getId());
            if (vd.getResult() == result) {
                title = "投票预测正确,  " + papertitel.substring(0, len) + "... 投稿成功";
                content = "AC: + 1    ";
            } else {
                title = "投票预测错误,  " + papertitel.substring(0, len) + "... 投稿失败";
                content = "AC: - 1    ";
            }
            Message msg = new Message(title, content, vd.getUser().getId());
            messageRepository.save(msg);
        }
    }


    /**
     * 系统计算项目AC消息
     * @param acRecords
     */
    public void autoSetProjectAcMessage(List<AcRecord> acRecords) {
        for (AcRecord ac : acRecords) {
            Message msg = new Message(ac.getReason(), "AC: + " + ac.getAc(), ac.getUser().getId());
            messageRepository.save(msg);
        }
    }


    /**
     * 手动计算项目AC消息
     * @param acRecords
     */
    public void manualSetProjectAcMessage(List<AcRecord> acRecords) {
        for (AcRecord ac : acRecords) {
            Message msg = new Message(ac.getReason(), "AC: + " + ac.getAc(), ac.getUser().getId());
            messageRepository.save(msg);
        }
    }


    /**
     * 项目bug消息
     * @param acRecords
     */
    public void bugMessage(List<AcRecord> acRecords) {
        for (AcRecord ac : acRecords) {
            Message msg = new Message(ac.getReason(), "AC: " + ac.getAc(), ac.getUser().getId());
            messageRepository.save(msg);
        }
    }


}
