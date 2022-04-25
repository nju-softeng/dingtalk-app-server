package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Patent;
import org.springframework.stereotype.Repository;

@Repository
public interface PatentRepository extends CustomizedRepository<Patent,Integer>{
}
