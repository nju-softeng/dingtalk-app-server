package com.softeng.dingtalk.entity;

import com.softeng.dingtalk.enums.PermissionEnum;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.po.PrizePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiXiaoKang
 * @since 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User实体对象", description="用户实体")
public class User implements Serializable {
    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "钉钉用户id")
    private String userid;

    @ApiModelProperty(value = "钉钉文档解释：员工在当前开发者企业账号范围内的唯一标识，系统生成，固定值，不会改变")
    private String unionid;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户管理权限")
    private List<Permission> permissionList;

    @ApiModelProperty(value = "用户所在研究组列表")
    private List<Permission> teamList;

    @ApiModelProperty(value = "用户职（学）位")
    private Position position;

    @ApiModelProperty(value = "插入时间")
    private LocalDateTime insertTime;

    @ApiModelProperty(value = "软删除标识")
    private boolean deleted;

    @ApiModelProperty(value = "本科学校")
    private String undergraduateCollege;

    @ApiModelProperty(value = "硕士学校")
    private String masterCollege;

    @ApiModelProperty(value = "身份证号")
    private String idCardNo;

    @ApiModelProperty(value = "银行卡号")
    private String creditCard;

    @ApiModelProperty(value = "开户行")
    private String bankName;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "状态true为在实习，false为在校")
    private Boolean workState;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "获奖情况")
    private List<PrizePo> allPrizes;

}
