package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.Patent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PatentRepository extends CustomizedRepository<Patent,Integer>, JpaSpecificationExecutor<Patent> {
}
