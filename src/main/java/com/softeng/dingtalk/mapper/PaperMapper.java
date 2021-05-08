package com.softeng.dingtalk.mapper;

import com.softeng.dingtalk.vo.PaperInfoVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description Paper mybatis 映射器
 * @date 26/06/2020
 */
@Repository
public interface PaperMapper {
    /**
     * 分页查询论文数据
     * @param offset
     * @param size
     * @return
     */
    List<PaperInfoVO> listPaperInfo(int offset, int size);

    @Select("select count(id) from paper")
    Integer countPaper();
}
