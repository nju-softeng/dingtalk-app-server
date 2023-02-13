package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.MessagePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author zhanyeye
 * @description
 * @date 3/2/2020
 */
public interface MessageRepository extends CustomizedRepository<MessagePo, Integer> {
   Page<MessagePo> findByUid(int uid, Pageable pageable);
}
