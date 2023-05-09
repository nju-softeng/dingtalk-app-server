package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.Reimbursement;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReimbursementRepository extends CustomizedRepository<Reimbursement,Integer>, JpaSpecificationExecutor<Reimbursement> {

}
