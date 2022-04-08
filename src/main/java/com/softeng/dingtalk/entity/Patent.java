package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Patent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //专利名字
    String name;

    //权利人
    @OneToOne
    User obligee;

    //发明人
    @OneToMany
    @JoinColumn(name = "patent_id")
    List<User> inventors;

    //版本
    String version;

    //受理文件路径
    String handlingFilePath;

    //受理文件id
    String handlingFileId;

    //受理文件路径
    String authorizationFilePath;

    //受理文件id
    String authorizationFileId;

    //状态： 0待内审，1内审不通过，2内审通过，3专利授权，4专利驳回
    int status=0;


}
