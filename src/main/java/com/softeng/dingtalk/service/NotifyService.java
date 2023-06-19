package com.softeng.dingtalk.service;

import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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
    InternalPaperRepository internalPaperRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    ExternalPaperRepository externalPaperRepository;
    @Autowired
    NotifyService notifyService;


    /**
     * dc 审核消息
     * @param dc
     */
    public void reviewDcMessage(DcRecord dc) {
        String month =  String.valueOf(dc.getYearmonth() % 100);
        messageRepository.save(new Message(
                month + "月第" + dc.getWeek() + "周绩效",
                "C值: " + dc.getCvalue() + "DC值: " + dc.getDc() + ",  AC值: " + dc.getAc(),
                dc.getApplicant().getId()
        ));
    }


    /**
     * dc 审核结果更新消息
     * @param dc
     */
    public void updateDcMessage(DcRecord dc) {
        reviewDcMessage(dc);
    }


    /**
     * 查询指定用户的消息
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public Page<Message> listUserMessage(int uid, int page, int size) {
        return messageRepository.findByUid(
                uid,
                PageRequest.of(page, size, Sort.by("createTime").descending())
        );
    }

    public String subString(String msg, int len) {
        return msg.substring(0, Math.min(len, msg.length()));
    }

    /**
     * 论文AC消息
     * @param internalPaper 最新的内部论文对象
     */
    public void paperAcMessage(InternalPaper internalPaper) {
        String title = "论文: " +
                subString(internalPaper.getTitle(), 20) +
                (internalPaper.hasAccepted() ? "... 投稿成功":(internalPaper.hasRejected() ? "... 投稿失败":"... 投稿中止"));

        messageRepository.saveAll(internalPaper.getPaperDetails().stream()
                .filter(paperDetail -> paperDetail.getAcRecord() != null)
                .map(paperDetail -> new Message(
                        title,
                        "AC: " + paperDetail.getAcRecord().getAc(),
                        paperDetail.getUser().getId()))
                .collect(Collectors.toList())
        );
    }

    private String generateMessage(Boolean voteResult, int finalResult, String paperTitle){
        if (finalResult == 2){
            return paperTitle+"... 投稿中止";
        }else if(finalResult == 1){
            if (voteResult){
                return "投票预测正确,  " + paperTitle + "... 投稿成功";
            }else {
                return "投票预测失败,  " + paperTitle + "... 投稿成功";
            }
        }else {
            if (voteResult){
                return "投票预测失败,  " + paperTitle + "... 投稿成功";
            }else {
                return "投票预测正确,  " + paperTitle + "... 投稿失败";
            }
        }
    }
    /**
     * 投票AC消息
     * @param vid
     * @param result
     */
    public void voteAcMessage(int vid, int result) {
        Vote v = voteRepository.findById(vid).get();
        String paperTitle = v.isExternal() ?
                "外部评审：" + subString(externalPaperRepository.findByVid(vid).getTitle(), 20) :
                "内部评审：" + subString(internalPaperRepository.findByVid(vid).getTitle(), 20);
        messageRepository.saveAll(v.getVoteDetails().stream()
                .map(voteDetail -> new Message(
                        "投票预测结果",
                        generateMessage(voteDetail.isResult(), result, paperTitle),
                        voteDetail.getUser().getId())
                )
                .collect(Collectors.toList())
        );
    }


    /**
     * 系统计算项目AC消息
     * @param acRecords
     */
    public void autoSetProjectAcMessage(List<AcRecord> acRecords) {
        messageRepository.saveAll(acRecords.stream()
                .map(acRecord -> new Message(
                        acRecord.getReason(),
                        "AC: " + acRecord.getAc(),
                        acRecord.getUser().getId()
                ))
                .collect(Collectors.toList())
        );
    }


    /**
     * 手动计算项目AC消息
     * @param acRecords
     */
    public void manualSetProjectAcMessage(List<AcRecord> acRecords) {
        notifyService.autoSetProjectAcMessage(acRecords);
    }


    /**
     * 项目bug消息
     * @param acRecords
     */
    public void bugMessage(List<AcRecord> acRecords) {
        notifyService.autoSetProjectAcMessage(acRecords);
    }


}
