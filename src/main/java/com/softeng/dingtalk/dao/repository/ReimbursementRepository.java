package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.Reimbursement;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReimbursementRepository extends CustomizedRepository<Reimbursement,Integer>, JpaSpecificationExecutor<Reimbursement> {

}
