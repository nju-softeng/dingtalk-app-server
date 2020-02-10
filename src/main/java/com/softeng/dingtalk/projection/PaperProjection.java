package com.softeng.dingtalk.projection;

import com.softeng.dingtalk.entity.PaperDetail;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 2/9/2020
 */
public interface PaperProjection {
    Integer getId();
    String getTitle();
    List<PaperDetail> getPaperDetails();
}
