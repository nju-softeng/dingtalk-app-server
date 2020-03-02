package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanyeye
 * @description
 * @date 3/2/2020
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
