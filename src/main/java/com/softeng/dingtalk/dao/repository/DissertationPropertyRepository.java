package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.DissertationPo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DissertationPropertyRepository extends CustomizedRepository<DissertationPo, Integer>{

    @Query("select diss from DissertationPo diss where diss.user.id =:uid")
    DissertationPo findByUserId(int uid);
}
