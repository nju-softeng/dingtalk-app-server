package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Message;
import com.softeng.dingtalk.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
    MessageRepository messageRepository;

    public void reviewDcMessage(int uid, double dc, double ac) {



    }

    public void updateDcMessage() {
        String title = "周报绩效被更新";
      //  String content = "DC: " + dc + ";  " + "AC: " + ac + "; " +

    }

    public void paperAcMessage() {

    }

    public void voteAcMessage() {

    }

}
