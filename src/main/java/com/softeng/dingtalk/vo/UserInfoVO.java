package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description
 * @create 3/27/2020 8:09 PM
 */
@Setter
@Getter
@AllArgsConstructor
public class UserInfoVO {
    private String name;
    private String avatar;
    private Position position;
    private String stuNum;
    private String undergraduateCollege;
    private String masterCollege;
    private String idCardNo;
    private String creditCard;
    private String bankName;
    private LocalDate rentingStart;
    private LocalDate rentingEnd;
    private String address;
    private Boolean workState;
    private String remark;
    private String leaseContractFileName;
    private String leaseContractFilePath;
    private String tel;
}
