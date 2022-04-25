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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "patent_inventor",joinColumns = {@JoinColumn(name = "patent_id")},inverseJoinColumns = {@JoinColumn(name = "user_id")})
    List<User> inventors;


    //版本
    String version;

    //文件目录
    String filePath;

    //受理文件名
    String handlingFileName;

    //受理文件id
    String handlingFileId;

    //授权文件名
    String authorizationFileName;

    //授权文件id
    String authorizationFileId;

    //状态： 0待内审，1内审不通过，2内审通过，3专利授权，4专利驳回
    int status=0;


}
