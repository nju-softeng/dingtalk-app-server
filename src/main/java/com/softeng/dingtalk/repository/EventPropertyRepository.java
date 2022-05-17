package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.EventProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface EventPropertyRepository extends CustomizedRepository<EventProperty, Integer>{
}
