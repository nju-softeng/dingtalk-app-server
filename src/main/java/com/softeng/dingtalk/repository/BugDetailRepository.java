package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.BugDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 3/22/2020
 */
@Repository
public interface BugDetailRepository extends CustomizedRepository<BugDetail, Integer> {

    void deleteBugDetailByBugId(int id);

    /**
     * 指定用户被确认bug的Id
     * @param uid
     * @return
     */
    @Query("select bd.bug.id from BugDetail bd where bd.user.id = :uid")
    List<Integer> listBugidByuid(@Param("uid") int uid);
}
