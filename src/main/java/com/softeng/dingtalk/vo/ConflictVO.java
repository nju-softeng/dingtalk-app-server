package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConflictVO {
    AcRecord mysqlData;
    AcRecord fabricData;
    String choice;
}
