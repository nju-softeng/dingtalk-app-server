package com.softeng.pms.repository;

import com.softeng.pms.entity.Paper;
import com.softeng.pms.entity.PaperDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface PaperDetailRepository extends CustomizedRepository<PaperDetail, Integer> {
    void deleteByPaper(Paper paper);

    List<PaperDetail> findByPaper(Paper paper);

    /**
     * 查询论文的作者id
     * @param pid 论文id
     * @return
     */
    @Query("select pd.user.id from PaperDetail pd where pd.paper.id = :pid")
    Set<Integer> listAuthorIdByPid(@Param("pid") int pid);

    /**
     * 删除论文记录
     * @param id
     */
    @Modifying
    @Query("delete from PaperDetail pd where pd.paper.id = :id")
    void deleteByPaperid(@Param("id") int id);


    @Query("select pd.user.name from PaperDetail pd where pd.paper.id = :pid")
    List<String> listPaperAuthor(@Param("pid") int pid);


}
