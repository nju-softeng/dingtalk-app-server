package com.softeng.dingtalk.repository.impl;

import com.softeng.dingtalk.repository.CustomizedRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author zhanyeye
 * @description 自定义接口实现类，继承SimpleJpaRepository类，从而注入EntityManager对象
 * @date 12/7/2019
 */
public class CustomizedRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomizedRepository<T, ID> {

    private EntityManager em;

    public CustomizedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
    }

    /**
     * @Description 基于entityManager对象，实现refresh()方法
     * @Author zhanyeye
     * @Date 9:10 PM 12/7/2019
     **/
    @Override
    public T refresh(T t) {
        em.refresh(t);
        return t;
    }

    @Override
    public void saveBatch(List<T> entities) {
        for (int i = 0; i < entities.size(); i++) {
            em.persist(entities.get(i));
            if ((i % 20) == 0) {
                em.flush();
            }
        }
    }

}