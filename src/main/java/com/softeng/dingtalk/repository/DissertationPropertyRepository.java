package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Dissertation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DissertationPropertyRepository extends CustomizedRepository<Dissertation, Integer>{

    @Query("select diss from Dissertation diss where diss.user.id =:uid")
    Dissertation findByUserId(int uid);
}
