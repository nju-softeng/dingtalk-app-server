package com.softeng.dingtalk.mapper;

import com.softeng.dingtalk.vo.PaperInfoVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 26/06/2020
 */
@Repository
public interface PaperMapper {
    List<PaperInfoVO> listPaperInfo();
}
