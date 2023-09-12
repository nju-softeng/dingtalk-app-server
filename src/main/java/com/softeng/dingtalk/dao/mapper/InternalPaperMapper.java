package com.softeng.dingtalk.dao.mapper;

import com.softeng.dingtalk.vo.PaperInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description InternalPaper mybatis 映射器
 * @date 26/06/2020
 */
@Repository
public interface InternalPaperMapper {
    /**
     * 分页查询论文数据
     * @param offset
     * @param size
     * @return
     */
    List<PaperInfoVO> listInternalPaperInfo(int offset, int size);

    List<PaperInfoVO> listNonFirstPaperInfo(int offset, int size);

    @Select("select count(id) from internal_paper where is_student_first_author = true or is_student_first_author is null")
    Integer countPaper();

    @Select("select count(id) from internal_paper where is_student_first_author = false")
    Integer countNonFirstPaper();
}
