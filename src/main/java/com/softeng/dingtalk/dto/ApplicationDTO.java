package com.softeng.dingtalk.dto;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description  用户前端提交的申请信息
 * @create 12/13/2019 9:17 PM
 */
@Getter
@Setter
public class ApplicationDTO {
    LocalDate date;
    private DcRecord dcRecord;        //DC值申请
    private List<AcItem> acItems;     //ac值申请列表      //todo 注意可能会有空指针
    public ApplicationDTO(DcRecord dcRecord, List<AcItem> acItems) {
        this.dcRecord = dcRecord;
        this.acItems = acItems;
    }
}
