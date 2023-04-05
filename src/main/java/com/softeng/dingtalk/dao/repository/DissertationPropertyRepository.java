package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.Dissertation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DissertationPropertyRepository extends CustomizedRepository<Dissertation, Integer>{

    @Query("select diss from Dissertation diss where diss.user.id =:uid")
    Dissertation findByUserId(int uid);
}
