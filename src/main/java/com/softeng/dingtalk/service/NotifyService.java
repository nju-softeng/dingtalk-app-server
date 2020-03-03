package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.Message;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.MessageRepository;
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

    public void paperAcMessage() {

    }

    public void voteAcMessage() {

    }

}
