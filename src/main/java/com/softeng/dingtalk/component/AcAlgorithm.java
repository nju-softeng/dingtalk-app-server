package com.softeng.dingtalk.component;

import com.softeng.dingtalk.po_entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lilingj
 * @date 2021/12/17
 */
@Slf4j
@Component
public class AcAlgorithm {
    /**
     * 不提交周报的扣分标准
     * @param user 我猜测以后可能不同用户扣不同的分，预留个user参数
     */
    public static double getPointOfUnsubmittedWeekReport(User user) {
        return -1.0;
    }
}
