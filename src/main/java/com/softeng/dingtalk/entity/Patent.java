package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Proxy(lazy = false)
@Table(name = "patent")
public class Patent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //专利名字
    String name;

    //权利人
    String obligee;

    @OneToOne
    User applicant;


    //发明人

    @OneToMany(mappedBy = "patent",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    List<PatentDetail> patentDetailList;

    //年份
    String year;
    //类型
    String type;


    //版本
    String version;

    //文件目录
    String filePath;


    //专利内审文件名字
    String reviewFileName;

    //专利内审文件id
    String reviewFileId;

    //专利提交文件名字
    String submissionFileName;

    //专利提交文件id
    String submissionFileId;

    //专利评论文件名字
    String commentFileName;

    //专利评论文件id
    String commentFileId;

    //受理文件名
    String handlingFileName;

    //受理文件id
    String handlingFileId;

    //授权文件名
    String authorizationFileName;

    //授权文件id
    String authorizationFileId;

    //状态： 0待内审，1内审不通过，2内审通过，3专利授权，4专利驳回
    int state=0;


    public Patent(String name, String year, String type, String version, String obligee, String filePath) {
        this.year=year;
        this.type=type;
        this.name = name;
        this.version = version;
        this.obligee=obligee;
        this.filePath = filePath;
    }

    public void update(String name, String year,String type,String version, String obligee, String filePath){
        this.year=year;
        this.type=type;
        this.name = name;
        this.version = version;
        this.obligee=obligee;
        this.filePath = filePath;
    }
}
