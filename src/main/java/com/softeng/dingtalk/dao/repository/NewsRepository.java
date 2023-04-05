package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends CustomizedRepository<News, Integer>{

//    @Query("select n from News n where n.isDeleted = :isDeleted")
    @Query(value = "select * from news n where n.is_deleted = :isDeleted", nativeQuery = true)
    Page<News> findAllByIsDeleted(@Param("isDeleted") int isDeleted, Pageable pageable);

//    @Query("select n from News n where n.isDeleted = :isDeleted and n.isShown = :isShown")
    @Query(value = "select * from news n where n.is_deleted = :isDeleted and n.is_shown = :isShown", nativeQuery = true)
    Page<News> findAllByIsShownAndIsDeleted(@Param("isShown") int isShown, @Param("isDeleted") int isDeleted, Pageable pageable);

//    @Query("select n from News n where n.isDeleted = :isDeleted and n.isShown = :isShown")
    @Query(value = "select * from news n where n.is_deleted = :isDeleted and n.is_shown = :isShown", nativeQuery = true)
    List<News> findAllByIsShownAndIsDeleted(@Param("isShown") int isShown, @Param("isDeleted") int isDeleted);

    @Modifying
    @Query("update News set isShown = :isShown where id = :newsId")
    void updateIsShown(@Param("newsId") int newsId, @Param("isShown") int isShown);

    @Modifying
    @Query("update News set isDeleted = :isDeleted where id = :newsId")
    void updateIsDeleted(@Param("newsId") int newsId, @Param("isDeleted") int isDeleted);
}
