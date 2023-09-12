package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "dissertation")
public class Dissertation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    //论文状态，0 预答辩，1 送审，2 最终答辩，3 最终版本
    int state=0;
    String filePath;
    String graduateYear;
    //预答辩
    String preRejoinFileName;
    String preRejoinFileId;
    //送审
    String reviewFileName;
    String reviewFileId;
    //最终答辩
    String rejoinFileName;
    String rejoinFileId;
    //最终版本
    String finalFileName;
    String finalFileId;

    public void update(int state,String graduateYear){
        this.state=state;
        this.graduateYear=graduateYear;
    }

    public Dissertation(int state, String graduateYear, String filePath){
        this.state=state;
        this.graduateYear=graduateYear;
        this.filePath=filePath;
    }
}
