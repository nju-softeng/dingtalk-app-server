package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.InternshipPeriodRecommended;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipPeriodRecommendedRepository extends CustomizedRepository<InternshipPeriodRecommended, Integer>{

    @Query(value = "select * from internship_period_recommended i order by i.release_time desc limit 1", nativeQuery = true)
    InternshipPeriodRecommended findTop();

}
