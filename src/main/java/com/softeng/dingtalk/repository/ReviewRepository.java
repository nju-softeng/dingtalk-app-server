package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Review;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 6/6/2020
 */
@Repository
public interface ReviewRepository  extends CustomizedRepository<Review, Integer> {
    public List<Review> findAllByPaperid(int paperid, Sort sort);
}
