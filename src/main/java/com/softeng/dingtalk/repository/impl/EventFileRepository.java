package com.softeng.dingtalk.repository.impl;

import com.softeng.dingtalk.entity.EventFile;
import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.repository.CustomizedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventFileRepository extends CustomizedRepository<EventFile, Integer> {

}
