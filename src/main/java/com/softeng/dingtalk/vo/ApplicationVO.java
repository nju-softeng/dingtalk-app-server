package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 展示待审核申请数据给审核人
 * @create 1/19/2020 7:16 PM
 */
@Setter
@Getter
@AllArgsConstructor
public class ApplicationVO {
    DcRecordVO dcRecordVO;
    List<AcItem> acItems;
}
