package com.softeng.dingtalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author zhanyeye
 * @description
 * @date 3/26/2020
 */
@NoRepositoryBean
public interface SoftDeleteCustomizedRepository<T, ID>  extends JpaRepository<T, ID> {
    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.id = ?1 and e.deleted = false")
    Optional<T> findById(ID id);


    @Override
    @Transactional
    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.deleted = false")
    List<T> findAll();

    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.id in ?1 and e.deleted = false")
    List<T> findAllById(Iterable<ID> ids);

    @Override
    @Transactional
    @Query("select count(e) from #{#entityName} e where e.deleted = false")
    long count();
}
