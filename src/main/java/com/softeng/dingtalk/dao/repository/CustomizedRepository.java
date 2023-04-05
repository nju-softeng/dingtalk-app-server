package com.softeng.dingtalk.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author zhanyeye
 * @description  自定义支持 refresh() 方法的全局基本接口
 * @date 12/7/2019
 */
@NoRepositoryBean    // 禁止spring按组件创建对象
public interface CustomizedRepository<T, ID> extends JpaRepository<T, ID> {

    T refresh(T t);  // 声明refresh()方法

    void saveBatch(List<T> entities);
}

