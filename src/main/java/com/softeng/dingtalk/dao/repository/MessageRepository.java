package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author zhanyeye
 * @description
 * @date 3/2/2020
 */
public interface MessageRepository extends CustomizedRepository<Message, Integer> {
   Page<Message> findByUid(int uid, Pageable pageable);
}
