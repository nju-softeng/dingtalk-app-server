package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.EventProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPropertyRepository extends CustomizedRepository<EventProperty, Integer>{

}
