package com.softeng.dingtalk.dto;

import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.DcRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhanyeye
 * @description 审核人提交的审核结果
 * @create 12/27/2019 9:58 AM
 */
@Getter
@Setter
@AllArgsConstructor
public class AuditDTO {
    private DcRecord dcRecord;
    private List<AcRecord> acRecords;
}
